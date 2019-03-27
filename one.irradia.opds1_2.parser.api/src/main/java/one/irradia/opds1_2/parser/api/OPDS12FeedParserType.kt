package one.irradia.opds1_2.parser.api

import one.irradia.opds1_2.api.OPDS12Feed
import one.irradia.opds1_2.api.OPDS12ParseResult

/**
 * An instance of a feed parser.
 */

interface OPDS12FeedParserType {

  /**
   * Parse the current element, returning results or errors.
   */

  fun parse(): OPDS12ParseResult<OPDS12Feed>

}