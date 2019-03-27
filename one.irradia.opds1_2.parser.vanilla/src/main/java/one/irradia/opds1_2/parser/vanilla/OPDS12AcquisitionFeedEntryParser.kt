package one.irradia.opds1_2.parser.vanilla

import com.google.common.base.Preconditions
import one.irradia.opds1_2.api.OPDS12Acquisition
import one.irradia.opds1_2.api.OPDS12AcquisitionFeedEntry
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation.values
import one.irradia.opds1_2.api.OPDS12Category
import one.irradia.opds1_2.api.OPDS12ElementType
import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import one.irradia.opds1_2.api.OPDS12Identifiers
import one.irradia.opds1_2.api.OPDS12Identifiers.ATOM_URI
import one.irradia.opds1_2.api.OPDS12IndirectAcquisition
import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.commons.OPDS12XMLParseError
import one.irradia.opds1_2.commons.OPDS12XMLProcessor
import one.irradia.opds1_2.lexical.OPDS12LexicalPosition
import one.irradia.opds1_2.parser.api.OPDS12AcquisitionFeedEntryParserType
import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserProviderType
import org.slf4j.LoggerFactory
import org.w3c.dom.Element
import org.xml.sax.SAXParseException
import java.net.URI
import java.util.IdentityHashMap

internal class OPDS12AcquisitionFeedEntryParser internal constructor(
  private val uri: URI,
  private val elementSupplier: () -> Element,
  private val extensionParsers: List<OPDS12AcquisitionFeedEntryExtensionParserProviderType>)
  : OPDS12AcquisitionFeedEntryParserType {

  private val logger = LoggerFactory.getLogger(OPDS12AcquisitionFeedEntryParser::class.java)
  private val errors = mutableListOf<OPDS12ParseResult.OPDS12ParseError>()
  private val elementMap = IdentityHashMap<OPDS12ElementType, Element>()
  private lateinit var element: Element

  private val producerName =
    "one.irradia.opds1_2.parser.vanilla.OPDS12AcquisitionFeedEntryParsers"

  private val xmlProcessor =
    OPDS12XMLProcessor(
      currentDocument = this.uri,
      producer = producerName,
      errors = this::publishXMLError)

  private fun publishXMLError(error: OPDS12XMLParseError) {
    this.errors.add(OPDS12ParseResult.OPDS12ParseError(
      producer = error.producer,
      lexical = error.lexical,
      message = error.message,
      exception = error.exception))
  }

  override fun parse(): OPDS12ParseResult<OPDS12AcquisitionFeedEntry> {
    return try {
      this.element = this.elementSupplier.invoke()

      this.xmlProcessor.requireElementIs(this.element, ATOM_URI, "entry")

      val title =
        this.xmlProcessor.optionalElementTextOrEmpty(this.element, ATOM_URI, "title")
      val id =
        this.xmlProcessor.requireElementText(this.element, ATOM_URI, "id")
      val summary =
        this.xmlProcessor.optionalElementTextOrEmpty(this.element, ATOM_URI, "summary")
      val updated =
        this.xmlProcessor.optionalElementInstant(this.element, ATOM_URI, "updated")
      val published =
        this.xmlProcessor.optionalElementInstant(this.element, ATOM_URI, "published")

      val authors =
        this.xmlProcessor.allChildElementsWithName(this.element, ATOM_URI, "author")
          .map { element -> this.authorOf(element) }
          .filterNotNull()

      val categories =
        this.xmlProcessor.allChildElementsWithName(this.element, ATOM_URI, "category")
          .map { element -> this.categoryOf(element) }
          .filterNotNull()

      val links =
        this.xmlProcessor.allChildElementsWithName(this.element, ATOM_URI, "link")

      val acquisitions =
        links.mapNotNull { element ->
          this.acquisitionOf(element)
        }

      val cover =
        links.mapNotNull { element ->
          this.xmlProcessor.optionalIndirectURIAttribute(
            element = element,
            attributeName = "rel",
            attributeValue = OPDS12Identifiers.IMAGE_URI.toString(),
            attributeExtract = "href")
        }.firstOrNull()

      val thumbnail =
        links.mapNotNull { element ->
          this.xmlProcessor.optionalIndirectURIAttribute(
            element = element,
            attributeName = "rel",
            attributeValue = OPDS12Identifiers.THUMBNAIL_URI.toString(),
            attributeExtract = "href")
        }.firstOrNull()

      val alternate =
        links.mapNotNull { element ->
          this.xmlProcessor.optionalIndirectURIAttribute(
            element = element,
            attributeName = "rel",
            attributeValue = "alternate",
            attributeExtract = "href")
        }.firstOrNull()

      val related =
        links.mapNotNull { element ->
          this.xmlProcessor.optionalIndirectURIAttribute(
            element = element,
            attributeName = "rel",
            attributeValue = "related",
            attributeExtract = "href")
        }.firstOrNull()

      /*
       * Assuming that there have been no errors, run all available extension parsers.
       */

      if (this.errors.isEmpty()) {
        val entry =
          OPDS12AcquisitionFeedEntry(
            acquisitions = acquisitions,
            authors = authors,
            cover = cover,
            categories = categories,
            id = id!!,
            related = related,
            published = published,
            summary = summary,
            thumbnail = thumbnail,
            title = title!!,
            updated = updated,
            alternate = alternate,
            extensions = listOf())

        val context = object: OPDS12AcquisitionFeedEntryExtensionParserContextType {
          override val documentURI: URI
            get() = this@OPDS12AcquisitionFeedEntryParser.uri
          override val entryWithoutExtensions: OPDS12AcquisitionFeedEntry
            get() = entry
          override val xmlElement: Element
            get() = this@OPDS12AcquisitionFeedEntryParser.element
          override fun xmlElementFor(element: OPDS12ElementType): Element? {
            return elementMap[element]
          }
        }

        val extensions =
          this.extensionParsers
            .map { provider -> provider.createParser(context) }
            .map { parser -> parser.parse() }

        val extensionErrors =
          extensions.filterIsInstance(OPDS12ParseResult.OPDS12ParseFailed::class.java)
            .flatMap(OPDS12ParseResult.OPDS12ParseFailed<*>::errors)

        if (extensionErrors.isEmpty()) {
          val extensionsOK =
            extensions.flatMap(this::extensionValues)

          OPDS12ParseResult.OPDS12ParseSucceeded(entry.copy(extensions = extensionsOK))
        } else {
          this.errors.addAll(extensionErrors)
          OPDS12ParseResult.OPDS12ParseFailed(this.errors.toList())
        }
      } else {
        OPDS12ParseResult.OPDS12ParseFailed(this.errors.toList())
      }
    } catch (e: SAXParseException) {
      val error =
        OPDS12ParseResult.OPDS12ParseError(
          producer = this.producerName,
          lexical = OPDS12LexicalPosition(this.uri, e.lineNumber, e.columnNumber),
          message = e.message!!,
          exception = e)

      this.errors.add(error)
      OPDS12ParseResult.OPDS12ParseFailed(errors = this.errors.toList())
    } catch (e: Exception) {
      val error =
        OPDS12ParseResult.OPDS12ParseError(
          producer = this.producerName,
          lexical = OPDS12LexicalPosition(this.uri, -1, -1),
          message = e.message!!,
          exception = e)

      this.errors.add(error)
      OPDS12ParseResult.OPDS12ParseFailed(errors = this.errors.toList())
    }
  }

  private fun extensionValues(
    result: OPDS12ParseResult<List<OPDS12ExtensionValueType>>): List<OPDS12ExtensionValueType> {
    return when (result) {
      is OPDS12ParseResult.OPDS12ParseSucceeded -> result.result
      is OPDS12ParseResult.OPDS12ParseFailed -> listOf()
    }
  }

  private fun acquisitionOf(element: Element): OPDS12Acquisition? {
    val relationText =
      this.xmlProcessor.attributeValue(element, "rel")

    return if (relationText != null) {
      val relation =
        optionallyOPDSAcquisitionRelation(relationText)

      if (relation != null) {
        val hrefURI =
          this.xmlProcessor.requireAttributeURI(element, "href")
        val type =
          this.xmlProcessor.optionalAttributeMIMEType(element, "type")
        val indirectAcquisitions =
          indirectAcquisitionsOf(element)

        if (hrefURI != null) {
          this.publishElement(
            result = OPDS12Acquisition(
              relation = relation,
              uri = hrefURI,
              type = type,
              indirectAcquisitions = indirectAcquisitions),
            element = element)
        } else {
          null
        }
      } else {
        null
      }
    } else {
      null
    }
  }

  private fun indirectAcquisitionOf(element: Element): OPDS12IndirectAcquisition? {
    val type =
      this.xmlProcessor.requireAttributeMIMEType(element, "type")
    val indirectAcquisitions =
      indirectAcquisitionsOf(element)

    return if (type != null) {
      this.publishElement(OPDS12IndirectAcquisition(type, indirectAcquisitions), element)
    } else
      null
  }

  private fun <T : OPDS12ElementType> publishElement(result: T, element: Element): T {
    Preconditions.checkArgument(
      !this.elementMap.containsKey(result),
      "Must not publish an element (%s -> %s) more than once",
      result,
      element)
    this.elementMap[result] = element
    return result
  }

  private fun indirectAcquisitionsOf(element: Element): List<OPDS12IndirectAcquisition> {
    return this.xmlProcessor.allChildElementsWithName(
      element,
      OPDS12Identifiers.OPDS_URI,
      "indirectAcquisition")
      .mapNotNull { childElement -> indirectAcquisitionOf(childElement) }
  }

  private fun optionallyOPDSAcquisitionRelation(relationText: String): OPDS12AcquisitionRelation? {
    for (r in values()) {
      if (r.uri.toString() == relationText) {
        return r
      }
    }
    return null
  }

  private fun authorOf(element: Element): String? {
    return this.xmlProcessor.requireElementText(element, ATOM_URI, "name")
  }

  private fun categoryOf(element: Element): OPDS12Category? {
    val term =
      this.xmlProcessor.requireAttribute(element, "term")
    val scheme =
      this.xmlProcessor.requireAttribute(element, "scheme")
    val label =
      this.xmlProcessor.requireAttribute(element, "label")

    return if (term != null && scheme != null && label != null) {
      this.publishElement(OPDS12Category(scheme = scheme, term = term, label = label), element)
    } else {
      null
    }
  }
}