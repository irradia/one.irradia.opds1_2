package one.irradia.opds1_2.api

import one.irradia.mime.api.MIMEType

/**
 * A tree of indirect acquisitions.
 */

data class OPDS12IndirectAcquisition(

  /**
   * The MIME type of the indirectly obtainable content.
   */

  val type: MIMEType,

  /**
   * Zero or more nested indirect acquisitions.
   */

  val indirectAcquisitions: List<OPDS12IndirectAcquisition>): OPDS12ElementType

