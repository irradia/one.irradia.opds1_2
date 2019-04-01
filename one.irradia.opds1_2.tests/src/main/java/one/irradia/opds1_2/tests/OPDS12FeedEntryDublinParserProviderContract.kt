package one.irradia.opds1_2.tests

import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.dublin.OPDS12DublinFeedEntryParsers
import one.irradia.opds1_2.dublin.OPDS12DublinCoreValue
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import org.joda.time.Instant
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URI

abstract class OPDS12FeedEntryDublinParserProviderContract {

  abstract fun logger(): Logger

  abstract fun parsers(): OPDS12FeedEntryParserProviderType

  private fun resource(name: String): InputStream {
    val path = "/one/irradia/opds1_2/tests/$name"
    val url =
      OPDS12FeedEntryDublinParserProviderContract::class.java.getResource(path)
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
  fun testEntryOKMetadata() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-ok-dublin-core.xml"),
        extensionParsers = listOf(OPDS12DublinFeedEntryParsers()))

    val result = parser.parse()
    dumpParseResult(result)
    val success = result as OPDS12ParseResult.OPDS12ParseSucceeded
    val entry = success.result

    val extensions =
      success.result.extensions.filterIsInstance(OPDS12DublinCoreValue::class.java)

    Assert.assertEquals(3, entry.extensions.size)

    run {
      val value =
        extensions.filterIsInstance(OPDS12DublinCoreValue.Language::class.java).first()
      Assert.assertEquals("en", value.code)
    }

    run {
      val value =
        extensions.filterIsInstance(OPDS12DublinCoreValue.Publisher::class.java).first()
      Assert.assertEquals("Fog City Publishing, LLC", value.name)
    }

    run {
      val value =
        extensions.filterIsInstance(OPDS12DublinCoreValue.Issued::class.java).first()
      Assert.assertEquals(Instant.parse("2015-08-26"), value.date)
    }
  }

  @Test
  fun testEntryOKBadMetadata() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-ok-dublin-core-bad.xml"),
        extensionParsers = listOf(OPDS12DublinFeedEntryParsers()))

    val result = parser.parse()
    dumpParseResult(result)
    val failed = result as OPDS12ParseResult.OPDS12ParseFailed

    Assert.assertEquals(1, failed.errors.size)
  }
}
