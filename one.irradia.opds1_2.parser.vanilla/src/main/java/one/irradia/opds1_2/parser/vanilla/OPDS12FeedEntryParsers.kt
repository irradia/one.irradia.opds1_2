package one.irradia.opds1_2.parser.vanilla

import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserType
import one.irradia.opds1_2.parser.api.OPDS12FeedParseRequest

/**
 * A default provider of parsers.
 *
 * Note: This class must have a public no-argument constructor in order to be used correctly
 * from [java.util.ServiceLoader].
 */

class OPDS12FeedEntryParsers : OPDS12FeedEntryParserProviderType {

  override fun createParser(request: OPDS12FeedParseRequest): OPDS12FeedEntryParserType =
    when (request) {
      is OPDS12FeedParseRequest.OPDS12FeedParseRequestForElement ->
        OPDS12FeedEntryParser(
          request = request,
          elementSupplier = { request.element })
      is OPDS12FeedParseRequest.OPDS12FeedParseRequestForStream ->
        OPDS12FeedEntryParser(
          request = request,
          elementSupplier = {
            val document = OPDS12XMLReaderLexical.readXML(request.uri, request.stream)
            document.documentElement
          })
    }
}
