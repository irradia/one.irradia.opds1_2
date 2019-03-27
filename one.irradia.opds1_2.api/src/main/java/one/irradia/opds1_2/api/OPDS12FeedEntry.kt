package one.irradia.opds1_2.api

import java.net.URI
import org.joda.time.Instant

/**
 * An entry in a feed.
 */

data class OPDS12FeedEntry(

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

  val authors: List<OPDS12Author>,

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
   * The links present in the feed.
   */

  val links: List<OPDS12Link>,

  /**
   * A list of extension values specific to this entry.
   */

  val extensions: List<OPDS12ExtensionValueType>): OPDS12ElementType {

  /**
   * The link to a related feed.
   *
   * @see "https://specs.opds.io/opds-1.2#52-catalog-entry-relations"
   */

  val related: OPDS12Link? =
    this.links.find { link -> link.relation == "related" }

  /**
   * The link to a feed of recommendations.
   *
   * @see "https://specs.opds.io/opds-1.2#52-catalog-entry-relations"
   */

  val recommendations: OPDS12Link? =
    this.links.find { link -> link.relation == "recommendations" }

  /**
   * A link to which to report issues.
   */

  val issues: OPDS12Link? =
    this.links.find { link -> link.relation == "issues" }

  /**
   * Cover image.
   *
   * @see "https://specs.opds.io/opds-1.2#522-artwork-relations"
   */

  val cover: OPDS12Link? =
    this.links.find { link -> link.relation == "http://opds-spec.org/image" }

  /**
   * Thumbnail image.
   *
   * @see "https://specs.opds.io/opds-1.2#522-artwork-relations"
   */

  val thumbnail: OPDS12Link? =
    this.links.find { link -> link.relation == "http://opds-spec.org/image/thumbnail" }

  /**
   * The list of author names as a single comma-separated string
   */

  val authorsText: String =
    this.authors.map { author -> author.name }.joinToString()

}
