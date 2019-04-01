package one.irradia.opds1_2.dublin

import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserType

/**
 * A parser for Dublin Core extensions.
 *
 * The parser produces values of type [OPDS12DublinCoreValue].
 *
 * Note: This class must have a no-argument public constructor to work with [java.util.ServiceLoader].
 */

class OPDS12DublinFeedEntryParsers : OPDS12FeedEntryExtensionParserProviderType {

  override fun createParser(
    context: OPDS12FeedEntryExtensionParserContextType)
    : OPDS12FeedEntryExtensionParserType {
    return OPDS12DublinFeedEntryParser(context)
  }

}
