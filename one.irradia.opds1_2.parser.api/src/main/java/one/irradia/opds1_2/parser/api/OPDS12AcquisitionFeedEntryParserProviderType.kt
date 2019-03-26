package one.irradia.opds1_2.parser.api

import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserProviderType
import org.w3c.dom.Element
import java.io.InputStream
import java.net.URI

/**
 * A provider of acquisition entry parsers.
 */

interface OPDS12AcquisitionFeedEntryParserProviderType {

  /**
   * Create a new parser.
   */

  fun createParser(
    uri: URI,
    stream: InputStream,
    extensionParsers: List<OPDS12AcquisitionFeedEntryExtensionParserProviderType> = listOf())
    : OPDS12AcquisitionFeedEntryParserType

  /**
   * Create a new parser.
   */

  fun createParser(
    uri: URI,
    element: Element,
    extensionParsers: List<OPDS12AcquisitionFeedEntryExtensionParserProviderType> = listOf())
    : OPDS12AcquisitionFeedEntryParserType

}
