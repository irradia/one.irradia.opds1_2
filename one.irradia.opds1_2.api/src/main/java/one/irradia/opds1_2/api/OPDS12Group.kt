package one.irradia.opds1_2.api

import java.net.URI

/**
 * An OPDS facet group.
 */

data class OPDS12Group(

  /**
   * The group title.
   */

  val title: String,

  /**
   * The group URI.
   */

  val uri: URI,

  /**
   * The acquisition feed entries.
   */

  val entries: List<OPDS12AcquisitionFeedEntry>): OPDS12ElementType
