package one.irradia.opds1_2.dublin

import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import org.joda.time.Instant
import java.net.URI

/**
 * A Dublin Core value that can appear in a feed.
 */

sealed class OPDS12DublinCoreValue : OPDS12ExtensionValueType {

  /**
   * @see "http://www.dublincore.org/specifications/dublin-core/dcmi-terms/2012-06-14/#elements-publisher"
   */

  data class Publisher(

    /**
     * The publisher name.
     */

    val name: String)
    : OPDS12DublinCoreValue() {

    override val typeURI: URI =
      URI.create("http://purl.org/dc/elements/1.1/publisher")
  }

  /**
   * @see "http://www.dublincore.org/specifications/dublin-core/dcmi-terms/2012-06-14/#terms-issued"
   */

  data class Issued(

    /**
     * The issue date.
     */

    val date: Instant)
    : OPDS12DublinCoreValue() {

    override val typeURI: URI =
      URI.create("http://purl.org/dc/terms/issued")
  }

  /**
   * @see "http://www.dublincore.org/specifications/dublin-core/dcmi-terms/2012-06-14/#terms-language"
   */

  data class Language(

    /**
     * The language code.
     */

    val code: String)
    : OPDS12DublinCoreValue() {

    override val typeURI: URI =
      URI.create("http://purl.org/dc/terms/language")
  }
}
