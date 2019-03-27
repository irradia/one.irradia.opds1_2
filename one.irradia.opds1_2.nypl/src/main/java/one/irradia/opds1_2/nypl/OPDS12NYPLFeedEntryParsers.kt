package one.irradia.opds1_2.nypl

import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserType

/**
 * A parser for NYPL extensions.
 *
 * The parser produces values of type [OPDS12Availability].
 *
 * Note: This class must have a no-argument public constructor to work with [java.util.ServiceLoader].
 */

class OPDS12NYPLFeedEntryParsers : OPDS12FeedEntryExtensionParserProviderType {

  override fun createParser(
    context: OPDS12FeedEntryExtensionParserContextType)
    : OPDS12FeedEntryExtensionParserType {
    return OPDS12NYPLFeedEntryParser(context)
  }

}
