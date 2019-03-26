package one.irradia.opds1_2.nypl

import one.irradia.opds1_2.api.OPDS12Acquisition
import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import org.joda.time.Instant
import java.net.URI

/**
 * Availability information for an OPDS entry.
 */

sealed class OPDS12Availability : OPDS12ExtensionValueType {

  companion object {

    val TYPE_URI: URI =
      URI.create("urn:one.irradia.opds1_2.nypl.OPDS12Availability")

  }

  override val typeURI =
    TYPE_URI

  /**
   * The acquisition to which this availability information belongs
   */

  abstract val acquisition: OPDS12Acquisition

  /**
   * An item is on hold.
   */

  data class Held(
    override val acquisition: OPDS12Acquisition,
    val position: Int?,
    val startDate: Instant?,
    val endDate: Instant?,
    val revokeURI: URI?)
    : OPDS12Availability()

  /**
   * An item is on hold and is ready to be checked out now.
   */

  data class HeldReady(
    override val acquisition: OPDS12Acquisition,
    val endDate: Instant?,
    val revokeURI: URI?)
    : OPDS12Availability()

  /**
   * An item can be placed on hold.
   */

  data class Holdable(
    override val acquisition: OPDS12Acquisition)
    : OPDS12Availability()

  /**
   * An item is available for borrowing.
   */

  data class Loanable(
    override val acquisition: OPDS12Acquisition)
    : OPDS12Availability()

  /**
   * An item is loaned out to the viewer.
   */

  data class Loaned(
    override val acquisition: OPDS12Acquisition,
    val startDate: Instant?,
    val endDate: Instant?,
    val revokeURI: URI?)
    : OPDS12Availability()

  /**
   * An item is open access and therefore available for download.
   */

  data class OpenAccess(
    override val acquisition: OPDS12Acquisition)
    : OPDS12Availability()

  /**
   * An item has been revoked via whatever DRM system it uses, but the server has yet
   * to be notified of this fact.
   */

  data class Revoked(
    override val acquisition: OPDS12Acquisition,
    val revokeURI: URI)
    : OPDS12Availability()
}
