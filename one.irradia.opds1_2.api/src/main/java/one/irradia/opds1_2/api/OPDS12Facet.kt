package one.irradia.opds1_2.api

import java.net.URI

/**
 * An OPDS facet.
 *
 * @see "https://specs.opds.io/opds-1.2#4-facets"
 */

data class OPDS12Facet(

  /**
   * `true` if the facet is active
   */

  val isActive: Boolean,

  /**
   * The URI
   */

  val uri: URI,

  /**
   * The group
   */

  val group: String,

  /**
   * The title
   */

  val title: String,

  /**
   * The group type
   */

  val groupType: String?): OPDS12ElementType
