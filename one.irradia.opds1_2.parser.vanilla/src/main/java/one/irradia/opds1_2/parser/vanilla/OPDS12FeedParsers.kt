package one.irradia.opds1_2.parser.vanilla

import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedParserType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserType
import org.w3c.dom.Element
import java.io.InputStream
import java.net.URI

/**
 * A default provider of parsers.
 *
 * Note: This class must have a public no-argument constructor in order to be used correctly
 * from [java.util.ServiceLoader].
 */

class OPDS12FeedParsers : OPDS12FeedParserProviderType {

  override fun createParser(
    uri: URI,
    stream: InputStream,
    acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType,
    extensionParsers : List<OPDS12FeedExtensionParserProviderType>,
    extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType>)
    : OPDS12FeedParserType {
    return OPDS12FeedParser(
      uri = uri,
      elementSupplier = {
        val document = OPDS12XMLReaderLexical.readXML(uri, stream)
        document.documentElement
      },
      acquisitionFeedEntryParsers = acquisitionFeedEntryParsers,
      extensionEntryParsers = extensionEntryParsers,
      extensionParsers = extensionParsers)
  }

  override fun createParser(
    uri: URI,
    element: Element,
    acquisitionFeedEntryParsers: OPDS12FeedEntryParserProviderType,
    extensionParsers : List<OPDS12FeedExtensionParserProviderType>,
    extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType>)
    : OPDS12FeedParserType {
    return OPDS12FeedParser(
      uri = uri,
      elementSupplier = { element },
      acquisitionFeedEntryParsers = acquisitionFeedEntryParsers,
      extensionEntryParsers = extensionEntryParsers,
      extensionParsers = extensionParsers)
  }
}
