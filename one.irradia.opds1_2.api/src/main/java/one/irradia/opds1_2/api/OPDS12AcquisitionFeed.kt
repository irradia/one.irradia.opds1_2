package one.irradia.opds1_2.api

import org.joda.time.Instant
import java.net.URI

/**
 * An OPDS acquisition feed.
 */

data class OPDS12AcquisitionFeed(
  val entries: List<OPDS12AcquisitionFeedEntry>,
  val facetsByGroup: Map<String, List<OPDS12Facet>>,
  val facetsInOrder: List<OPDS12Facet>,
  val groups: Map<String, OPDS12Group>,
  val groupsInOrder: List<OPDS12Group>,
  val id: String,
  val next: URI?,
  val title: String,
  val updated: Instant,
  val uri: URI,
  val termsOfService: URI?,
  val about: URI?,
  val licenses: URI?,
  val privacyPolicy: URI?): OPDS12ElementType
