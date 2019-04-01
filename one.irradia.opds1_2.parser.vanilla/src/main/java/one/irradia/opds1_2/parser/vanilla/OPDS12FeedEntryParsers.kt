package one.irradia.opds1_2.parser.vanilla

import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import org.w3c.dom.Element
import java.io.InputStream
import java.net.URI

/**
 * A default provider of parsers.
 *
 * Note: This class must have a public no-argument constructor in order to be used correctly
 * from [java.util.ServiceLoader].
 */

class OPDS12FeedEntryParsers : OPDS12FeedEntryParserProviderType {

  override fun createParser(
    uri: URI,
    stream: InputStream,
    extensionParsers: List<OPDS12FeedEntryExtensionParserProviderType>)
    : OPDS12FeedEntryParserType {
    return OPDS12FeedEntryParser(
      uri = uri,
      elementSupplier = {
        val document = OPDS12XMLReaderLexical.readXML(uri, stream)
        document.documentElement
      },
      extensionParsers = extensionParsers)
  }

  override fun createParser(
    uri: URI,
    element: Element,
    extensionParsers: List<OPDS12FeedEntryExtensionParserProviderType>)
    : OPDS12FeedEntryParserType {
    return OPDS12FeedEntryParser(
      uri = uri,
      elementSupplier = { element },
      extensionParsers = extensionParsers)
  }

}
