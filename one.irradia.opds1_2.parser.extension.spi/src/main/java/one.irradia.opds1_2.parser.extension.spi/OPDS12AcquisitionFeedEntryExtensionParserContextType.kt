package one.irradia.opds1_2.parser.extension.spi

import one.irradia.opds1_2.api.OPDS12AcquisitionFeedEntry
import one.irradia.opds1_2.api.OPDS12ElementType
import org.w3c.dom.Element
import java.net.URI

/**
 * The context passed to acquisition feed entry extension parsers.
 */

interface OPDS12AcquisitionFeedEntryExtensionParserContextType {

  /**
   * The URI of the owning document.
   */

  val documentURI: URI

  /**
   * The acquisition feed entry minus any extensions.
   */

  val entryWithoutExtensions: OPDS12AcquisitionFeedEntry

  /**
   * The XML element
   */

  val xmlElement: Element

  /**
   * The XML elements used to construct elements of the entry
   */

  fun xmlElementFor(element: OPDS12ElementType): Element?
}
