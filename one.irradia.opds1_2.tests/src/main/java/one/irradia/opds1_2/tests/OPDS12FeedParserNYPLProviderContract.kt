package one.irradia.opds1_2.tests

import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.nypl.OPDS12NYPLFeedEntryParsers
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedParseRequest
import one.irradia.opds1_2.parser.api.OPDS12FeedParseTarget.OPDS12FeedParseTargetStream
import one.irradia.opds1_2.parser.api.OPDS12FeedParserProviderType
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.xml.sax.SAXParseException
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URI

abstract class OPDS12FeedParserNYPLProviderContract {

  abstract fun logger(): Logger

  abstract fun parsers(): OPDS12FeedParserProviderType

  abstract fun entryParsers(): OPDS12FeedEntryParserProviderType

  private fun resource(name: String): InputStream {
    val path = "/one/irradia/opds1_2/tests/$name"
    val url =
      OPDS12FeedParserNYPLProviderContract::class.java.getResource(path)
        ?: throw FileNotFoundException("No such resource: $path")
    return url.openStream()
  }

  private lateinit var parsers: OPDS12FeedParserProviderType
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
  fun testSetup() {
    this.parsers = this.parsers()
    this.logger = this.logger()
  }

  @Test
  fun testEmpty() {
    val parser =
      this.parsers.createParser(OPDS12FeedParseRequest(
        uri = URI.create("urn:test"),
        target = OPDS12FeedParseTargetStream(this.resource("empty.xml")),
        acquisitionFeedEntryParsers = this.entryParsers(),
        extensionEntryParsers = listOf(),
        extensionParsers = listOf()))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(1, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.exception, IsInstanceOf(SAXParseException::class.java))
    }
  }

  @Test
  fun testOrgArchiveMain20190327() {
    val parser =
      this.parsers.createParser(OPDS12FeedParseRequest(
        uri = URI.create("urn:test"),
        target = OPDS12FeedParseTargetStream(this.resource("feeds/org.archive-main-20190327.xml")),
        acquisitionFeedEntryParsers = this.entryParsers(),
        extensionEntryParsers = listOf(OPDS12NYPLFeedEntryParsers()),
        extensionParsers = listOf()))

    val result = parser.parse()
    this.dumpParseResult(result)
    val success = result as OPDS12ParseResult.OPDS12ParseSucceeded

    val feed = success.result
    Assert.assertTrue("Navigation feed", !feed.isAcquisitionFeed)
    Assert.assertEquals("https://bookserver.archive.org/", feed.baseURI.toString())
  }

  @Test
  fun testOrgLibrarySimplifiedMain20190327() {
    val parser =
      this.parsers.createParser(OPDS12FeedParseRequest(
        uri = URI.create("urn:test"),
        target = OPDS12FeedParseTargetStream(this.resource("feeds/org.librarysimplified-main-20190327.xml")),
        acquisitionFeedEntryParsers = this.entryParsers(),
        extensionEntryParsers = listOf(OPDS12NYPLFeedEntryParsers()),
        extensionParsers = listOf()))

    val result = parser.parse()
    this.dumpParseResult(result)
    val success = result as OPDS12ParseResult.OPDS12ParseSucceeded

    val feed = success.result
    Assert.assertTrue("Acquisition feed", feed.isAcquisitionFeed)
    Assert.assertEquals("urn:test", feed.baseURI.toString())
    Assert.assertEquals(132, feed.entries.size)
  }

  @Test
  fun testOrgLibrarySimplifiedClassics20190413() {
    val parser =
      this.parsers.createParser(OPDS12FeedParseRequest(
        uri = URI.create("urn:test"),
        target = OPDS12FeedParseTargetStream(this.resource("feeds/classics-20190413.xml")),
        acquisitionFeedEntryParsers = this.entryParsers(),
        extensionEntryParsers = listOf(OPDS12NYPLFeedEntryParsers()),
        extensionParsers = listOf()))

    val result = parser.parse()
    this.dumpParseResult(result)
    val success = result as OPDS12ParseResult.OPDS12ParseSucceeded

    val feed = success.result
    Assert.assertTrue("Acquisition feed", feed.isAcquisitionFeed)
    Assert.assertEquals("urn:test", feed.baseURI.toString())
    Assert.assertEquals(40, feed.entries.size)
  }
}
