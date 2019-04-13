package one.irradia.opds1_2.parser.api

import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserProviderType
import org.w3c.dom.Element
import java.io.InputStream
import java.net.URI

/**
 * The type of parse requests.
 */

sealed class OPDS12FeedParseRequest {

  /**
   * The URI of the document being parsed.
   */

  abstract val uri: URI

  /**
   * A provider of acquisition feed entry parsers.
   */

  abstract val acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType

  /**
   * The list of extension parsers.
   */

  abstract val extensionParsers: List<OPDS12FeedExtensionParserProviderType>

  /**
   * The list of entry extension parsers.
   */

  abstract val extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType>

  /**
   * A parse request for an element.
   */

  data class OPDS12FeedParseRequestForElement(

    /**
     * The XML element.
     */

    val element: Element,
    override val uri: URI,
    override val acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType,
    override val extensionParsers: List<OPDS12FeedExtensionParserProviderType> = listOf(),
    override val extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType> = listOf())
    : OPDS12FeedParseRequest()

  /**
   * A parse request for a stream.
   */

  data class OPDS12FeedParseRequestForStream(

    /**
     * The input stream.
     */

    val stream: InputStream,
    override val uri: URI,
    override val acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType,
    override val extensionParsers: List<OPDS12FeedExtensionParserProviderType> = listOf(),
    override val extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType> = listOf())
    : OPDS12FeedParseRequest()

}

