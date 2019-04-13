package one.irradia.opds1_2.parser.api

import org.w3c.dom.Element
import java.io.InputStream

/**
 * The target of a parsing operation.
 */

sealed class OPDS12FeedParseTarget {

  /**
   * The target is a stream.
   */

  data class OPDS12FeedParseTargetStream(

    /**
     * The input stream.
     */

    val inputStream: InputStream)
    : OPDS12FeedParseTarget()

  /**
   * The target is an element.
   */

  data class OPDS12FeedParseTargetElement(

    /**
     * The target element.
     */

    val element: Element)
    : OPDS12FeedParseTarget()

}