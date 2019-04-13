package one.irradia.opds1_2.parser.api

/**
 * A provider of entry parsers.
 */

interface OPDS12FeedEntryParserProviderType {

  /**
   * Create a new parser.
   */

  fun createParser(request: OPDS12FeedParseRequest): OPDS12FeedEntryParserType

}
