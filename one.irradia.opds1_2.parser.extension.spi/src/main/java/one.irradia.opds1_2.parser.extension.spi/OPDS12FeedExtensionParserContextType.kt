package one.irradia.opds1_2.parser.extension.spi

import one.irradia.opds1_2.api.OPDS12ElementType
import one.irradia.opds1_2.api.OPDS12Feed
import org.w3c.dom.Element
import java.net.URI

/**
 * The context passed to feed extension parsers.
 */

interface OPDS12FeedExtensionParserContextType {

  /**
   * The URI of the owning document.
   */

  val documentURI: URI

  /**
   * The feed minus any extensions.
   */

  val feedWithoutExtensions: OPDS12Feed

  /**
   * The XML element
   */

  val xmlElement: Element

  /**
   * The XML elements used to construct elements of the entry
   */

  fun xmlElementFor(element: OPDS12ElementType): Element?
}
