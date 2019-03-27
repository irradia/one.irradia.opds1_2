package one.irradia.opds1_2.parser.extension.spi

import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import one.irradia.opds1_2.api.OPDS12ParseResult

/**
 * A parser that is able to examine an XML element representing a feed entry and extract extension
 * data from it.
 */

interface OPDS12FeedEntryExtensionParserType {

  /**
   * Extract any extra data from the given element.
   */

  fun parse(): OPDS12ParseResult<List<OPDS12ExtensionValueType>>

}
