package one.irradia.opds1_2.dublin

import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserType

/**
 * A parser for Dublin Core extensions.
 *
 * The parser produces values of type [OPDS12DublinCoreValue].
 *
 * Note: This class must have a no-argument public constructor to work with [java.util.ServiceLoader].
 */

class OPDS12DublinFeedParsers : OPDS12FeedExtensionParserProviderType {

  override fun createParser(context: OPDS12FeedExtensionParserContextType): OPDS12FeedExtensionParserType {
    return OPDS12DublinFeedParser(context)
  }

}
