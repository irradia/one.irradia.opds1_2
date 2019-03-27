package one.irradia.opds1_2.dublin

import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.commons.OPDS12XMLParseError
import one.irradia.opds1_2.commons.OPDS12XMLProcessor
import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserType
import java.net.URI

/**
 * A feed entry parser for Dublin Core extensions.
 *
 * The parser produces values of type [OPDS12DublinCoreEntryValue].
 */

internal class OPDS12DublinAcquisitionFeedEntryParser(
  private val context: OPDS12AcquisitionFeedEntryExtensionParserContextType)
  : OPDS12AcquisitionFeedEntryExtensionParserType {

  private val errors = mutableListOf<OPDS12ParseResult.OPDS12ParseError>()
  private val values = mutableListOf<OPDS12ExtensionValueType>()

  private val namespace =
    URI.create("http://purl.org/dc/terms/")

  private val producerName =
    "one.irradia.opds1_2.dublin.OPDS12DublinAcquisitionFeedEntryParser"

  private val xmlProcessor =
    OPDS12XMLProcessor(
      currentDocument = this.context.documentURI,
      producer = this.producerName,
      errors = this::publishXMLError)

  private fun publishXMLError(error: OPDS12XMLParseError) {
    this.errors.add(OPDS12ParseResult.OPDS12ParseError(
      producer = error.producer,
      lexical = error.lexical,
      message = error.message,
      exception = error.exception))
  }

  override fun parse(): OPDS12ParseResult<List<OPDS12ExtensionValueType>> {

    this.xmlProcessor.optionalElementText(this.context.xmlElement, namespace, "publisher")
      ?.let { text -> OPDS12DublinCoreEntryValue.Publisher(text) }
      ?.let { value -> this.values.add(value) }

    this.xmlProcessor.optionalElementText(this.context.xmlElement, namespace, "language")
      ?.let { text -> OPDS12DublinCoreEntryValue.Language(text) }
      ?.let { value -> this.values.add(value) }

    this.xmlProcessor.optionalElementInstant(this.context.xmlElement, namespace, "issued")
      ?.let { text -> OPDS12DublinCoreEntryValue.Issued(text) }
      ?.let { value -> this.values.add(value) }

    return if (this.errors.isEmpty()) {
      OPDS12ParseResult.OPDS12ParseSucceeded(this.values.toList())
    } else {
      OPDS12ParseResult.OPDS12ParseFailed(this.errors.toList())
    }
  }
}
