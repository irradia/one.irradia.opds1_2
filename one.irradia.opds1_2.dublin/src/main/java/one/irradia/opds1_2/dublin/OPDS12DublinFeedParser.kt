package one.irradia.opds1_2.dublin

import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseError
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseFailed
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseSucceeded
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseWarning
import one.irradia.opds1_2.commons.OPDS12XMLParseError
import one.irradia.opds1_2.commons.OPDS12XMLParseWarning
import one.irradia.opds1_2.commons.OPDS12XMLProcessor
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserType
import java.net.URI

/**
 * A feed parser for Dublin Core extensions.
 *
 * The parser produces values of type [OPDS12DublinCoreValue].
 */

internal class OPDS12DublinFeedParser(
  private val context: OPDS12FeedExtensionParserContextType)
  : OPDS12FeedExtensionParserType {

  private val warnings = mutableListOf<OPDS12ParseWarning>()
  private val errors = mutableListOf<OPDS12ParseError>()
  private val values = mutableListOf<OPDS12ExtensionValueType>()

  private val namespace =
    URI.create("http://purl.org/dc/terms/")

  private val producerName =
    "one.irradia.opds1_2.dublin.OPDS12DublinFeedParser"

  private val xmlProcessor =
    OPDS12XMLProcessor(
      currentDocument = this.context.documentURI,
      producer = this.producerName,
      errors = this::publishXMLError,
      warnings = this::publishXMLWarning)

  private fun publishXMLWarning(warning: OPDS12XMLParseWarning) {
    this.warnings.add(OPDS12ParseWarning(
      producer = warning.producer,
      lexical = warning.lexical,
      message = warning.message,
      exception = warning.exception))
  }

  private fun publishXMLError(error: OPDS12XMLParseError) {
    this.errors.add(OPDS12ParseError(
      producer = error.producer,
      lexical = error.lexical,
      message = error.message,
      exception = error.exception))
  }

  override fun parse(): OPDS12ParseResult<List<OPDS12ExtensionValueType>> {

    this.xmlProcessor.optionalElementText(this.context.xmlElement, namespace, "publisher")
      ?.let { text -> OPDS12DublinCoreValue.Publisher(text) }
      ?.let { value -> this.values.add(value) }

    this.xmlProcessor.optionalElementText(this.context.xmlElement, namespace, "language")
      ?.let { text -> OPDS12DublinCoreValue.Language(text) }
      ?.let { value -> this.values.add(value) }

    this.xmlProcessor.optionalElementInstant(
      element = this.context.xmlElement,
      namespace = namespace,
      name = "issued",
      allowInvalid = this.context.configuration.allowInvalidTimestamps)
      ?.let { text -> OPDS12DublinCoreValue.Issued(text) }
      ?.let { value -> this.values.add(value) }

    return if (this.errors.isEmpty()) {
      OPDS12ParseSucceeded(
        warnings = this.warnings.toList(),
        result = this.values.toList())
    } else {
      OPDS12ParseFailed(
        warnings = this.warnings.toList(),
        errors = this.errors.toList())
    }
  }
}
