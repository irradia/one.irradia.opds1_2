package one.irradia.opds1_2.parser.api

/**
 * A provider of acquisition entry parsers.
 */

interface OPDS12FeedParserProviderType {

  /**
   * Create a new parser.
   */

  fun createParser(request: OPDS12FeedParseRequest): OPDS12FeedParserType

}
