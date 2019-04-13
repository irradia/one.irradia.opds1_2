package one.irradia.opds1_2.nypl

import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseFailed
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseSucceeded
import one.irradia.opds1_2.commons.OPDS12XMLParseError
import one.irradia.opds1_2.commons.OPDS12XMLParseWarning
import one.irradia.opds1_2.commons.OPDS12XMLProcessor
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserType

/**
 * A feed entry parser for NYPL extensions.
 *
 * The parser produces values of type [OPDS12Availability].
 */

internal class OPDS12NYPLFeedEntryParser(
  private val context: OPDS12FeedEntryExtensionParserContextType)
  : OPDS12FeedEntryExtensionParserType {

  private val warnings = mutableListOf<OPDS12ParseResult.OPDS12ParseWarning>()
  private val errors = mutableListOf<OPDS12ParseResult.OPDS12ParseError>()
  private val values = mutableListOf<OPDS12ExtensionValueType>()

  private val producerName =
    "one.irradia.opds1_2.nypl.OPDS12NYPLAcquisitionFeedEntryParser"

  private val xmlProcessor =
    OPDS12XMLProcessor(
      currentDocument = this.context.documentURI,
      producer = producerName,
      errors = this::publishXMLError,
      warnings = this::publishXMLWarning)

  private fun publishXMLWarning(warning: OPDS12XMLParseWarning) {
    this.warnings.add(OPDS12ParseResult.OPDS12ParseWarning(
      producer = warning.producer,
      lexical = warning.lexical,
      message = warning.message,
      exception = warning.exception))
  }
  
  private fun publishXMLError(error: OPDS12XMLParseError) {
    this.errors.add(OPDS12ParseResult.OPDS12ParseError(
      producer = error.producer,
      lexical = error.lexical,
      message = error.message,
      exception = error.exception))
  }

  override fun parse(): OPDS12ParseResult<List<OPDS12ExtensionValueType>> {

    for (acquisition in this.context.entryWithoutExtensions.acquisitions) {
      val element = this.context.xmlElementFor(acquisition)
      if (element != null) {
        val availability =
          OPDS12AvailabilityInference.availabilityOf(element, this.xmlProcessor, acquisition)
        this.values.add(availability)
      }
    }

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
