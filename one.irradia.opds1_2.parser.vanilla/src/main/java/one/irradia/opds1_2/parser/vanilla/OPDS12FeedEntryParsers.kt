package one.irradia.opds1_2.parser.vanilla

import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserType
import one.irradia.opds1_2.parser.api.OPDS12FeedParseRequest
import one.irradia.opds1_2.parser.api.OPDS12FeedParseTarget

/**
 * A default provider of parsers.
 *
 * Note: This class must have a public no-argument constructor in order to be used correctly
 * from [java.util.ServiceLoader].
 */

class OPDS12FeedEntryParsers : OPDS12FeedEntryParserProviderType {

  override fun createParser(request: OPDS12FeedParseRequest): OPDS12FeedEntryParserType {
    val target = request.target
    return when (target) {
      is OPDS12FeedParseTarget.OPDS12FeedParseTargetStream ->
        OPDS12FeedEntryParser(
          request = request,
          elementSupplier = {
            val document = OPDS12XMLReaderLexical.readXML(request.uri, target.inputStream)
            document.documentElement
          })
      is OPDS12FeedParseTarget.OPDS12FeedParseTargetElement ->
        OPDS12FeedEntryParser(
          request = request,
          elementSupplier = { target.element })
    }
  }
}
