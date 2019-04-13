package one.irradia.opds1_2.tests

import one.irradia.opds1_2.api.OPDS12Acquisition
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.nypl.OPDS12Availability
import one.irradia.opds1_2.nypl.OPDS12NYPLFeedEntryParsers
import one.irradia.opds1_2.parser.api.OPDS12FeedParseRequest
import one.irradia.opds1_2.parser.api.OPDS12FeedParseRequest.*
import one.irradia.opds1_2.parser.api.OPDS12FeedParseTarget
import one.irradia.opds1_2.parser.api.OPDS12FeedParseTarget.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URI
import org.joda.time.Instant

abstract class OPDS12FeedEntryNYPLParserProviderContract {

  abstract fun logger(): Logger

  abstract fun parsers(): OPDS12FeedEntryParserProviderType

  private fun resource(name: String): InputStream {
    val path = "/one/irradia/opds1_2/tests/$name"
    val url =
      OPDS12FeedEntryNYPLParserProviderContract::class.java.getResource(path)
        ?: throw FileNotFoundException("No such resource: $path")
    return url.openStream()
  }

  private lateinit var parsers: OPDS12FeedEntryParserProviderType
  private lateinit var logger: Logger

  private fun <T> dumpParseResult(result: OPDS12ParseResult<T>) {
    return when (result) {
      is OPDS12ParseResult.OPDS12ParseSucceeded -> {
        this.logger.debug("success: {}", result.result)
      }
      is OPDS12ParseResult.OPDS12ParseFailed -> {
        result.errors.forEach { error ->
          this.logger.debug("error: {}: ", error, error.exception)
        }
      }
    }
  }

  @Before
  fun testSetup()
  {
    this.parsers = this.parsers()
    this.logger = this.logger()
  }

  @Test
  fun testEntryOKAcquisitionsAvailability() {
    val parser =
      this.parsers.createParser(OPDS12FeedParseRequest(
        uri = URI.create("urn:test"),
        acquisitionFeedEntryParsers = this.parsers,
        target = OPDS12FeedParseTargetStream(this.resource("entry-ok-acquisitions-availability.xml")),
        extensionEntryParsers = listOf(OPDS12NYPLFeedEntryParsers())))

    val result = parser.parse()
    dumpParseResult(result)
    val success = result as OPDS12ParseResult.OPDS12ParseSucceeded
    val entry = success.result
    Assert.assertEquals(23, entry.acquisitions.size)

    val extensions =
      success.result.extensions.filterIsInstance(OPDS12Availability::class.java)

    run {
      val acquisition = entry.acquisitions[0]
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Open-Access0")
      Assert.assertEquals(
        OPDS12Availability.OpenAccess(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[1]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Loanable")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[2]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Loaned-Timed")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.Loaned(
        acquisition = acquisition,
        startDate = Instant.parse("2000-01-01T00:00:00Z"),
        endDate = Instant.parse("2010-01-01T00:00:00Z"),
        revokeURI = null
      ), availability)
    }

    run {
      val acquisition = entry.acquisitions[3]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Loaned-Indefinite")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.Loaned(
        acquisition = acquisition,
        startDate = Instant.parse("2000-01-01T00:00:00Z"),
        endDate = null,
        revokeURI = null
      ), availability)
    }

    run {
      val acquisition = entry.acquisitions[4]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Loanable-0")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[5]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Holdable-0")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Holdable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[6]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "HeldReady")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.HeldReady(
        acquisition = acquisition,
        endDate = null,
        revokeURI = null),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[7]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "HeldReady-Timed")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.HeldReady(
        acquisition = acquisition,
        endDate = Instant.parse("2010-01-01T00:00:00Z"),
        revokeURI = null),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[8]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Held-Ready-Specific")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.HeldReady(
        acquisition = acquisition,
        endDate = Instant.parse("2015-08-24T00:30:24.000Z"),
        revokeURI = null),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[9]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Held-Timed")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.Held(
        acquisition = acquisition,
        startDate = Instant.parse("2000-01-01T00:00:00Z"),
        position = null,
        endDate = Instant.parse("2010-01-01T00:00:00.000Z"),
        revokeURI = null),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[10]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Held-Timed-Queued")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.Held(
        acquisition = acquisition,
        startDate = Instant.parse("2000-01-01T00:00:00Z"),
        position = 3,
        endDate = Instant.parse("2010-01-01T00:00:00.000Z"),
        revokeURI = null),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[11]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Held-Indefinite")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.Held(
        acquisition = acquisition,
        startDate = Instant.parse("2000-01-01T00:00:00Z"),
        position = null,
        endDate = null,
        revokeURI = null),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[12]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Held-Indefinite-Queued")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(OPDS12Availability.Held(
        acquisition = acquisition,
        startDate = Instant.parse("2000-01-01T00:00:00Z"),
        position = 3,
        endDate = null,
        revokeURI = null),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[13]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Buy-Is-Loanable")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[14]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Subscribe-Is-Loanable")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[15]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Sample-Is-Loanable")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[16]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Generic-Is-Loaned")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loaned(
          acquisition,
          startDate = null,
          endDate = null,
          revokeURI = null),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[17]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Unavailable-Is-Holdable")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Holdable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[18]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "OpenAccess-Is-OpenAccess")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.OpenAccess(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[19]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Buy-Available-Is-Loanable2")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[20]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Subscribe-Available-Is-Loanable2")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[21]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Sample-Available-Is-Loanable2")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }

    run {
      val acquisition = entry.acquisitions[22]
      logger.debug("acquisition: {}", acquisition)
      checkAcquisitionURIEndsWith(acquisition, "Generic-Nonsense-Is-Loanable")
      val availability = extensions.find { available -> available.acquisition == acquisition }!!
      Assert.assertEquals(
        OPDS12Availability.Loanable(acquisition),
        availability)
    }
  }

  private fun checkAcquisitionURIEndsWith(acquisition: OPDS12Acquisition, name: String) {
    val received = acquisition.uri.toString()
    Assert.assertTrue("Wanted $name but got $received", received.endsWith(name))
  }
}
