package one.irradia.opds1_2.parser.api

import one.irradia.opds1_2.api.OPDS12AcquisitionFeedEntry
import one.irradia.opds1_2.api.OPDS12ParseResult

/**
 * An instance of an acquisition entry parser.
 */

interface OPDS12AcquisitionFeedEntryParserType {

  /**
   * Parse the current element, returning results or errors.
   */

  fun parse(): OPDS12ParseResult<OPDS12AcquisitionFeedEntry>

}