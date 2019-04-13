package one.irradia.opds1_2.parser.api

import one.irradia.opds1_2.api.OPDS12FeedParseConfiguration
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserProviderType
import java.net.URI

/**
 * The type of parse requests.
 */

data class OPDS12FeedParseRequest(

  /**
   * Configuration for the parser.
   */

  val configuration: OPDS12FeedParseConfiguration = OPDS12FeedParseConfiguration(),

  /**
   * The target element to parse.
   */

  val target: OPDS12FeedParseTarget,

  /**
   * The URI of the document being parsed.
   */

  val uri: URI,

  /**
   * A provider of acquisition feed entry parsers.
   */

  val acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType,

  /**
   * The list of extension parsers.
   */

  val extensionParsers: List<OPDS12FeedExtensionParserProviderType> = listOf(),

  /**
   * The list of entry extension parsers.
   */

  val extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType> = listOf())

