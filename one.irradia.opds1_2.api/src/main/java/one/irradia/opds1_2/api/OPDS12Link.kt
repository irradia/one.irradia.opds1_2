package one.irradia.opds1_2.api

import one.irradia.mime.api.MIMEType
import java.net.URI

/**
 * A generic Atom link.
 */

data class OPDS12Link(

  /**
   * The target of the link.
   */

  val href: URI,

  /**
   * The declared MIME type of the link, if any.
   */

  val type: MIMEType?,

  /**
   * The declared link relation, if any.
   */

  val relation: String?): OPDS12ElementType

