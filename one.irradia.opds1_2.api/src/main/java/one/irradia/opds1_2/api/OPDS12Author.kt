package one.irradia.opds1_2.api

import java.net.URI

/**
 * An author.
 */

data class OPDS12Author(

  /**
   * The name of the author.
   */

  val name: String,

  /**
   * The URI of the author.
   */

  val uri: URI?): OPDS12ElementType
