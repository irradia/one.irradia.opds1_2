package one.irradia.opds1_2.parser.vanilla

import com.google.common.base.Preconditions
import one.irradia.opds1_2.api.OPDS12ElementType
import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import one.irradia.opds1_2.api.OPDS12Facet
import one.irradia.opds1_2.api.OPDS12Feed
import one.irradia.opds1_2.api.OPDS12FeedEntry
import one.irradia.opds1_2.api.OPDS12Group
import one.irradia.opds1_2.api.OPDS12Identifiers.ATOM_URI
import one.irradia.opds1_2.api.OPDS12Link
import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.commons.OPDS12XMLParseError
import one.irradia.opds1_2.commons.OPDS12XMLProcessor
import one.irradia.opds1_2.lexical.OPDS12LexicalPosition
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedParserType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserProviderType
import org.w3c.dom.Element
import org.xml.sax.SAXParseException
import java.net.URI
import java.util.IdentityHashMap

internal class OPDS12FeedParser(
  private val uri: URI,
  private val elementSupplier: () -> Element,
  private val acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType,
  private val extensionParsers: List<OPDS12FeedExtensionParserProviderType>,
  private val extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType>)
  : OPDS12FeedParserType {

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

  override fun parse(): OPDS12ParseResult<OPDS12Feed> {
    return try {
      this.element = this.elementSupplier.invoke()

      this.xmlProcessor.requireElementIs(this.element, ATOM_URI, "feed")

      val baseURI =
        this.xmlProcessor.optionalAttributeURI(element, "xml:base")
      val title =
        this.xmlProcessor.optionalElementTextOrEmpty(this.element, ATOM_URI, "title")
      val id =
        this.xmlProcessor.requireElementText(this.element, ATOM_URI, "id")
      val updated =
        this.xmlProcessor.optionalElementInstant(this.element, ATOM_URI, "updated")
      val published =
        this.xmlProcessor.optionalElementInstant(this.element, ATOM_URI, "published")
      val facetsInOrder =
        listOf<OPDS12Facet>()
      val groupsInOrder =
        listOf<OPDS12Group>()

      val links =
        this.xmlProcessor.allChildElementsWithName(this.element, ATOM_URI, "link")
      val linksValues =
        links.mapNotNull(this::linkOf)

      val next = null
      val termsOfService = null
      val about = null
      val licenses = null
      val privacyPolicy = null

      val entries: List<OPDS12FeedEntry> =
        this.xmlProcessor.allChildElementsWithName(this.element, ATOM_URI, "entry")
          .map(this::parseEntry)
          .flatMap(this::consumeEntryParserResult)

      /*
       * Assuming that there have been no errors, run all available extension parsers.
       */

      if (this.errors.isEmpty()) {
        return runExtensions(OPDS12Feed(
          id = id!!,
          title = title,
          updated = updated,
          published = published,
          baseURI = baseURI ?: uri,
          uri = uri,
          links = linksValues,
          entries = entries,
          facetsInOrder = facetsInOrder,
          groupsInOrder = groupsInOrder,
          extensions = listOf()))
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

  private fun runExtensions(feed: OPDS12Feed): OPDS12ParseResult<OPDS12Feed> {
    val context = object : OPDS12FeedExtensionParserContextType {
      override val documentURI: URI
        get() = uri
      override val feedWithoutExtensions: OPDS12Feed
        get() = feed
      override val xmlElement: Element
        get() = this@OPDS12FeedParser.element

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

    return if (extensionErrors.isEmpty()) {
      val extensionsOK =
        extensions.flatMap(this::extensionValues)

      OPDS12ParseResult.OPDS12ParseSucceeded(feed.copy(extensions = extensionsOK))
    } else {
      this.errors.addAll(extensionErrors)
      OPDS12ParseResult.OPDS12ParseFailed(this.errors.toList())
    }
  }

  private fun extensionValues(
    result: OPDS12ParseResult<List<OPDS12ExtensionValueType>>): List<OPDS12ExtensionValueType> {
    return when (result) {
      is OPDS12ParseResult.OPDS12ParseSucceeded -> result.result
      is OPDS12ParseResult.OPDS12ParseFailed -> listOf()
    }
  }

  private fun parseEntry(entry: Element): OPDS12ParseResult<OPDS12FeedEntry> {
    return this.acquisitionFeedEntryParsers.createParser(
      uri = this.uri,
      element = entry,
      extensionParsers = this.extensionEntryParsers)
      .parse()
  }

  private fun consumeEntryParserResult(
    result: OPDS12ParseResult<OPDS12FeedEntry>): List<OPDS12FeedEntry> {
    return when (result) {
      is OPDS12ParseResult.OPDS12ParseSucceeded ->
        listOf(result.result)
      is OPDS12ParseResult.OPDS12ParseFailed -> {
        this.errors.addAll(result.errors)
        listOf()
      }
    }
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

  private fun linkOf(element: Element): OPDS12Link? {
    val href =
      this.xmlProcessor.requireAttributeURI(element, "href")
    val type =
      this.xmlProcessor.optionalAttributeMIMEType(element, "type")
    val relation =
      this.xmlProcessor.optionalAttribute(element, "rel")

    return if (href != null) {
      this.publishElement(OPDS12Link(href = href, type = type, relation = relation), element)
    } else {
      null
    }
  }
}
