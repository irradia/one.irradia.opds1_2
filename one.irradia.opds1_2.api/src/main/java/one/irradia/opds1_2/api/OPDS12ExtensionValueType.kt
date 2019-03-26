package one.irradia.opds1_2.api

import java.net.URI

/**
 * Extension content defined outside of the OPDS standard but delivered inside the
 * feed as extra content.
 */

interface OPDS12ExtensionValueType {

  /**
   * The URI that describes the extension value.
   */

  val typeURI: URI

}
