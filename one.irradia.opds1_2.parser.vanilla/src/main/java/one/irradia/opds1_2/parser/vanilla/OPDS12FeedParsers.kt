package one.irradia.opds1_2.parser.vanilla

import one.irradia.opds1_2.parser.api.OPDS12FeedParseRequest
import one.irradia.opds1_2.parser.api.OPDS12FeedParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedParserType

/**
 * A default provider of parsers.
 *
 * Note: This class must have a public no-argument constructor in order to be used correctly
 * from [java.util.ServiceLoader].
 */

class OPDS12FeedParsers : OPDS12FeedParserProviderType {

  override fun createParser(request: OPDS12FeedParseRequest): OPDS12FeedParserType =
    when (request) {
      is OPDS12FeedParseRequest.OPDS12FeedParseRequestForElement ->
        OPDS12FeedParser(
          request = request,
          elementSupplier = { request.element })
      is OPDS12FeedParseRequest.OPDS12FeedParseRequestForStream ->
        OPDS12FeedParser(
          request = request,
          elementSupplier = {
            val document = OPDS12XMLReaderLexical.readXML(request.uri, request.stream)
            document.documentElement
          })
    }
}
