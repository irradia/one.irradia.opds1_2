package one.irradia.opds1_2.nypl

import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.commons.OPDS12XMLParseError
import one.irradia.opds1_2.commons.OPDS12XMLProcessor
import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserType

class OPDS12NYPLAcquisitionFeedEntryParser(
  private val context: OPDS12AcquisitionFeedEntryExtensionParserContextType)
  : OPDS12AcquisitionFeedEntryExtensionParserType {

  private val errors = mutableListOf<OPDS12ParseResult.OPDS12ParseError>()
  private val values = mutableListOf<OPDS12ExtensionValueType>()

  private val producerName =
    "one.irradia.opds1_2.nypl.OPDS12NYPLAcquisitionFeedEntryParser"

  private val xmlProcessor =
    OPDS12XMLProcessor(
      currentDocument = this.context.documentURI,
      producer = producerName,
      errors = this::publishXMLError)

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

    if (this.errors.isEmpty()) {
      return OPDS12ParseResult.OPDS12ParseSucceeded(this.values.toList())
    } else {
      return OPDS12ParseResult.OPDS12ParseFailed(this.errors.toList())
    }
  }
}
