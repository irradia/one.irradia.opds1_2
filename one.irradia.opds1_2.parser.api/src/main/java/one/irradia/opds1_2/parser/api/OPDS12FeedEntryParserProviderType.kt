package one.irradia.opds1_2.parser.api

import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import org.w3c.dom.Element
import java.io.InputStream
import java.net.URI

/**
 * A provider of entry parsers.
 */

interface OPDS12FeedEntryParserProviderType {

  /**
   * Create a new parser.
   */

  fun createParser(
    uri: URI,
    stream: InputStream,
    extensionParsers: List<OPDS12FeedEntryExtensionParserProviderType> = listOf())
    : OPDS12FeedEntryParserType

  /**
   * Create a new parser.
   */

  fun createParser(
    uri: URI,
    element: Element,
    extensionParsers: List<OPDS12FeedEntryExtensionParserProviderType> = listOf())
    : OPDS12FeedEntryParserType

}
