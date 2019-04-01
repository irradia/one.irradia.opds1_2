package one.irradia.opds1_2.api

import one.irradia.mime.api.MIMEType
import java.net.URI

/**
 * An OPDS acquisition.
 */

data class OPDS12Acquisition(

  /**
   * The acquisition relation.
   */

  val relation: OPDS12AcquisitionRelation,

  /**
   * The URI that must be used to retrieve the content.
   */

  val uri: URI,

  /**
   * The type of the directly obtainable content.
   */

  val type: MIMEType?,

  /**
   * The tree of indirect acquisitions.
   */

  val indirectAcquisitions: List<OPDS12IndirectAcquisition>): OPDS12ElementType
