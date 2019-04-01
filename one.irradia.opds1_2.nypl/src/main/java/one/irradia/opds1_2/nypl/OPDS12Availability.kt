package one.irradia.opds1_2.nypl

import one.irradia.opds1_2.api.OPDS12Acquisition
import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import org.joda.time.Instant
import java.net.URI

/**
 * Availability information for an OPDS entry.
 *
 * @see "https://github.com/NYPL-Simplified/Simplified/wiki/OPDS-For-Library-Patrons"
 */

sealed class OPDS12Availability : OPDS12ExtensionValueType {

  companion object {

    /**
     * The type URI for the extension value.
     */

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

    /**
     * The reader's current position in the queue.
     */

    val position: Int?,

    /**
     * The date the reader entered the queue.
     */

    val startDate: Instant?,

    /**
     * The estimated time at which the hold will become available.
     */

    val endDate: Instant?,

    /**
     * The revocation URI
     */

    val revokeURI: URI?)
    : OPDS12Availability()

  /**
   * An item is on hold and is ready to be checked out now.
   */

  data class HeldReady(
    override val acquisition: OPDS12Acquisition,

    /**
     * The estimated time at which the hold will become unavailable.
     */

    val endDate: Instant?,

    /**
     * The revocation URI
     */

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

    /**
     * The creation time of the loan.
     */

    val startDate: Instant?,

    /**
     * The estimated time at which the loan will expire.
     */

    val endDate: Instant?,

    /**
     * The revocation URI
     */

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

    /**
     * The revocation URI
     */

    val revokeURI: URI)
    : OPDS12Availability()
}
