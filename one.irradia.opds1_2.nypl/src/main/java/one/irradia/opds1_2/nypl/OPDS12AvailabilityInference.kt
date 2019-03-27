package one.irradia.opds1_2.nypl

import one.irradia.opds1_2.api.OPDS12Acquisition
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation.ACQUISITION_BORROW
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation.ACQUISITION_BUY
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation.ACQUISITION_GENERIC
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation.ACQUISITION_OPEN_ACCESS
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation.ACQUISITION_SAMPLE
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation.ACQUISITION_SUBSCRIBE
import one.irradia.opds1_2.api.OPDS12Identifiers.OPDS_URI
import one.irradia.opds1_2.commons.OPDS12XMLProcessor
import org.w3c.dom.Element

/**
 * Functions to infer the availability of OPDS elements.
 *
 * @see "https://github.com/NYPL-Simplified/Simplified/wiki/OPDS-For-Library-Patrons"
 */

object OPDS12AvailabilityInference {

  private fun mightBeHoldable(
    xmlProcessor: OPDS12XMLProcessor,
    copies: Element?,
    acquisition: OPDS12Acquisition): OPDS12Availability {

    return if (copies != null) {
      val copiesAvailable =
        xmlProcessor.optionalAttributeInt(copies, "available") ?: 0
      if (copiesAvailable > 0)
        OPDS12Availability.Loanable(acquisition)
      else
        OPDS12Availability.Holdable(acquisition)
    } else
      OPDS12Availability.Loanable(acquisition)
  }

  /**
   * Determine the availability of the given element.
   */

  fun availabilityOf(
    element: Element,
    xmlProcessor: OPDS12XMLProcessor,
    acquisition: OPDS12Acquisition): OPDS12Availability {

    val availability =
      xmlProcessor.firstChildElementWithName(element, OPDS_URI, "availability")
    val holds =
      xmlProcessor.firstChildElementWithName(element, OPDS_URI, "holds")
    val copies =
      xmlProcessor.firstChildElementWithName(element, OPDS_URI, "copies")

    /*
     * If there is no availability element, then the availability determination
     * has to be made purely upon the acquisition relation.
     */

    return if (availability == null) {
      withoutAvailability(acquisition, xmlProcessor, copies)
    } else {
      withAvailability(holds, xmlProcessor, availability, acquisition, copies)
    }
  }

  private fun withAvailability(
    holds: Element?,
    xmlProcessor: OPDS12XMLProcessor,
    availability: Element,
    acquisition: OPDS12Acquisition,
    copies: Element?
  ): OPDS12Availability {

    val position =
      holds?.let { xmlProcessor.optionalAttributeInt(holds, "position") }
    val startDate =
      xmlProcessor.optionalAttributeInstant(availability, "since")
    val endDate =
      xmlProcessor.optionalAttributeInstant(availability, "until")

    /*
     * If there is availability information, the book might be loanable, loaned,
     * or holdable.
     */

    val status = xmlProcessor.requireAttribute(availability, "status")
    return when (status) {
      "unavailable" ->
        OPDS12Availability.Holdable(acquisition)

      "available" ->
        when (acquisition.relation) {
          ACQUISITION_OPEN_ACCESS ->
            OPDS12Availability.OpenAccess(acquisition)

          ACQUISITION_SUBSCRIBE,
          ACQUISITION_SAMPLE,
          ACQUISITION_BUY,
          ACQUISITION_BORROW ->
            OPDS12Availability.Loanable(acquisition)

          ACQUISITION_GENERIC ->
            OPDS12Availability.Loaned(
              acquisition = acquisition,
              startDate = startDate,
              endDate = endDate,
              revokeURI = null)
        }

      "reserved" ->
        OPDS12Availability.Held(
          acquisition = acquisition,
          position = position,
          startDate = startDate,
          endDate = endDate,
          revokeURI = null)

      "ready" ->
        OPDS12Availability.HeldReady(
          acquisition = acquisition,
          endDate = endDate,
          revokeURI = null)

      else ->
        mightBeHoldable(xmlProcessor, copies, acquisition)
    }
  }

  private fun withoutAvailability(
    acquisition: OPDS12Acquisition,
    xmlProcessor: OPDS12XMLProcessor,
    copies: Element?): OPDS12Availability {

    return when (acquisition.relation) {
      ACQUISITION_BORROW ->
        mightBeHoldable(xmlProcessor, copies, acquisition)

      ACQUISITION_BUY ->
        OPDS12Availability.Loanable(acquisition)

      ACQUISITION_GENERIC ->
        OPDS12Availability.Loaned(
          acquisition = acquisition,
          startDate = null,
          endDate = null,
          revokeURI = null)

      ACQUISITION_OPEN_ACCESS ->
        OPDS12Availability.OpenAccess(acquisition)

      ACQUISITION_SAMPLE ->
        OPDS12Availability.Loanable(acquisition)

      ACQUISITION_SUBSCRIBE ->
        OPDS12Availability.Loanable(acquisition)
    }
  }
}
