package one.irradia.opds1_2.parser.api

import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserType
import org.w3c.dom.Element
import java.io.InputStream
import java.net.URI

/**
 * A provider of acquisition entry parsers.
 */

interface OPDS12FeedParserProviderType {

  /**
   * Create a new parser.
   */

  fun createParser(
    uri: URI,
    stream: InputStream,
    acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType,
    extensionParsers: List<OPDS12FeedExtensionParserProviderType> = listOf(),
    extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType> = listOf())
    : OPDS12FeedParserType

  /**
   * Create a new parser.
   */

  fun createParser(
    uri: URI,
    element: Element,
    acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType,
    extensionParsers: List<OPDS12FeedExtensionParserProviderType> = listOf(),
    extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType> = listOf())
    : OPDS12FeedParserType

}
