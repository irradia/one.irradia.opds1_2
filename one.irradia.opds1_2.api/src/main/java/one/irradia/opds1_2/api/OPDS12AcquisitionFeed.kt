package one.irradia.opds1_2.api

import org.joda.time.Instant
import java.net.URI

/**
 * An OPDS acquisition feed.
 */

data class OPDS12AcquisitionFeed(

  /**
   * The list of entries.
   */

  val entries: List<OPDS12AcquisitionFeedEntry>,

  /**
   * The facets grouped by name.
   */

  val facetsByGroup: Map<String, List<OPDS12Facet>>,

  /**
   * The facets in declaration order.
   */

  val facetsInOrder: List<OPDS12Facet>,

  /**
   * The available groups by name.
   */

  val groups: Map<String, OPDS12Group>,

  /**
   * The groups in declaration order.
   */

  val groupsInOrder: List<OPDS12Group>,

  /**
   * The feed ID.
   */

  val id: String,

  /**
   * The next feed, if any.
   */

  val next: URI?,

  /**
   * The feed title.
   */

  val title: String,

  /**
   * The time of last update for the feed.
   */

  val updated: Instant,

  /**
   * The URI of the feed.
   */

  val uri: URI,

  /**
   * The terms of service link for the feed, if any.
   */

  val termsOfService: URI?,

  /**
   * The about link for the feed, if any.
   */

  val about: URI?,

  /**
   * The licenses link for the feed, if any.
   */

  val licenses: URI?,

  /**
   * The privacy policy link for the feed, if any.
   */

  val privacyPolicy: URI?): OPDS12ElementType
