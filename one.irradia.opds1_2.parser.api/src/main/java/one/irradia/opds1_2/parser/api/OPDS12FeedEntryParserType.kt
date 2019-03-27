package one.irradia.opds1_2.parser.api

import one.irradia.opds1_2.api.OPDS12FeedEntry
import one.irradia.opds1_2.api.OPDS12ParseResult

/**
 * An instance of an entry parser.
 */

interface OPDS12FeedEntryParserType {

  /**
   * Parse the current element, returning results or errors.
   */

  fun parse(): OPDS12ParseResult<OPDS12FeedEntry>

}