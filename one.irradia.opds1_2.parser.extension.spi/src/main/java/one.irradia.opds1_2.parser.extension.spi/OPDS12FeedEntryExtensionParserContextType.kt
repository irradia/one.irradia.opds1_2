package one.irradia.opds1_2.parser.extension.spi

import one.irradia.opds1_2.api.OPDS12ElementType
import one.irradia.opds1_2.api.OPDS12FeedEntry
import one.irradia.opds1_2.api.OPDS12FeedParseConfiguration
import org.w3c.dom.Element
import java.net.URI

/**
 * The context passed to feed entry extension parsers.
 */

interface OPDS12FeedEntryExtensionParserContextType {

  /**
   * The configuration data used for the parser.
   */

  val configuration: OPDS12FeedParseConfiguration

  /**
   * The URI of the owning document.
   */

  val documentURI: URI

  /**
   * The feed entry minus any extensions.
   */

  val entryWithoutExtensions: OPDS12FeedEntry

  /**
   * The XML element
   */

  val xmlElement: Element

  /**
   * The XML elements used to construct elements of the entry
   */

  fun xmlElementFor(element: OPDS12ElementType): Element?
}
