package one.irradia.opds1_2.api

import java.net.URI
import org.joda.time.Instant

/**
 * An entry in an acquisition feed.
 */

data class OPDS12AcquisitionFeedEntry(

  /**
   * The acquisitions available in the entry.
   *
   * @see "https://specs.opds.io/opds-1.2#521-acquisition-relations"
   */

  val acquisitions: List<OPDS12Acquisition>,

  /**
   * Authors for the work.
   *
   * @see "https://specs.opds.io/opds-1.2#511-relationship-between-atom-and-dublin-core-metadata"
   */

  val authors: List<String>,

  /**
   * Cover image.
   *
   * @see "https://specs.opds.io/opds-1.2#522-artwork-relations"
   */

  val cover: URI?,

  /**
   * Entry categories.
   *
   * @see "https://specs.opds.io/opds-1.2#511-relationship-between-atom-and-dublin-core-metadata"
   */

  val categories: List<OPDS12Category>,

  /**
   * Unique identifier for the work.
   *
   * @see "https://specs.opds.io/opds-1.2#511-relationship-between-atom-and-dublin-core-metadata"
   */

  val id: String,

  /**
   * Related feeds.
   *
   * @see "https://specs.opds.io/opds-1.2#52-catalog-entry-relations"
   */

  val related: URI?,

  /**
   * The time the feed was published.
   *
   * @see "https://specs.opds.io/opds-1.2#511-relationship-between-atom-and-dublin-core-metadata"
   */

  val published: Instant?,

  /**
   * A summary of the feed.
   *
   * @see "https://specs.opds.io/opds-1.2#511-relationship-between-atom-and-dublin-core-metadata"
   */

  val summary: String,

  /**
   * Thumbnail image.
   *
   * @see "https://specs.opds.io/opds-1.2#522-artwork-relations"
   */

  val thumbnail: URI?,

  /**
   * Feed title.
   */

  val title: String,

  /**
   * The time the feed was updated.
   *
   * @see "https://specs.opds.io/opds-1.2#511-relationship-between-atom-and-dublin-core-metadata"
   */

  val updated: Instant?,

  /**
   * A link to the full entry if the entry is partial.
   *
   * @see "https://specs.opds.io/opds-1.2#512-partial-and-complete-catalog-entries"
   */

  val alternate: URI?,

  /**
   * A list of extension values specific to this entry.
   */

  val extensions: List<OPDS12ExtensionValueType>): OPDS12ElementType
