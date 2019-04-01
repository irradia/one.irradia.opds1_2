package one.irradia.opds1_2.tests

import one.irradia.mime.vanilla.MIMEParser
import one.irradia.opds1_2.api.OPDS12AcquisitionRelation
import one.irradia.opds1_2.api.OPDS12Author
import one.irradia.opds1_2.api.OPDS12Category
import one.irradia.opds1_2.api.OPDS12ExtensionValueType
import one.irradia.opds1_2.api.OPDS12ParseResult
import one.irradia.opds1_2.lexical.OPDS12LexicalPosition
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserType
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.StringContains
import org.joda.time.Instant
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.xml.sax.SAXParseException
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URI

abstract class OPDS12FeedEntryParserProviderContract {

  abstract fun logger(): Logger

  abstract fun parsers(): OPDS12FeedEntryParserProviderType

  private fun resource(name: String): InputStream {
    val path = "/one/irradia/opds1_2/tests/$name"
    val url =
      OPDS12FeedEntryParserProviderContract::class.java.getResource(path)
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
  fun testSetup() {
    this.parsers = this.parsers()
    this.logger = this.logger()
  }

  @Test
  fun testEmpty() {
    val parser =
      this.parsers.createParser(URI.create("urn:test"), this.resource("empty.xml"))

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
  fun testEntryWrongNamespace() {
    val parser =
      this.parsers.createParser(URI.create("urn:test"), this.resource("entry-wrong-namespace.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(2, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Unexpected element"))
    }
  }

  @Test
  fun testEntryWrongElement() {
    val parser =
      this.parsers.createParser(URI.create("urn:test"), this.resource("entry-wrong-element.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(2, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Unexpected element"))
    }
  }

  @Test
  fun testEntryExtensionFailed() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-ok.xml"),
        extensionParsers = listOf(FailingExtensionParserProvider()))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(1, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Failed!"))
    }
  }

  @Test
  fun testEntryOK() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-ok.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val success = result as OPDS12ParseResult.OPDS12ParseSucceeded
    val entry = success.result
    Assert.assertEquals("title", entry.title)
    Assert.assertEquals("id", entry.id)
    Assert.assertEquals(OPDS12Author("Author", null), entry.authors[0])
    Assert.assertEquals(OPDS12Category("http://schema.org/audience", "AdultTerm", "AdultLabel"), entry.categories[0])
    Assert.assertEquals(Instant.parse("2000-01-01T00:00:01Z"), entry.published)
    Assert.assertEquals(Instant.parse("2000-01-01T00:00:00Z"), entry.updated)

    run {
      val acquisition = entry.acquisitions[0]
      Assert.assertEquals(MIMEParser.parseRaisingException("application/atom+xml;type=entry;profile=opds-catalog"), acquisition.type)
      Assert.assertEquals(OPDS12AcquisitionRelation.ACQUISITION_BORROW, acquisition.relation)
      Assert.assertEquals(URI.create("http://example.com/borrow"), acquisition.uri)
    }
  }

  @Test
  fun testEntryOKEmpties() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-ok-empties.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val success = result as OPDS12ParseResult.OPDS12ParseSucceeded

    val entry = success.result
    Assert.assertEquals("", entry.title)
    Assert.assertEquals("id", entry.id)
    Assert.assertEquals(OPDS12Author("Author", null), entry.authors[0])
    Assert.assertEquals(OPDS12Category("http://schema.org/audience", "AdultTerm", "AdultLabel"), entry.categories[0])
    Assert.assertEquals(Instant.parse("2000-01-01T00:00:01Z"), entry.published)
    Assert.assertEquals(Instant.parse("2000-01-01T00:00:00Z"), entry.updated)

    run {
      val acquisition = entry.acquisitions[0]
      Assert.assertEquals(MIMEParser.parseRaisingException("application/atom+xml;type=entry;profile=opds-catalog"), acquisition.type)
      Assert.assertEquals(OPDS12AcquisitionRelation.ACQUISITION_BORROW, acquisition.relation)
      Assert.assertEquals(URI.create("http://example.com/borrow"), acquisition.uri)
    }
  }

  @Test
  fun testEntryOKWithPrefix() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-ok-with-prefix.xml"))

    val result = parser.parse()
    val success = result as OPDS12ParseResult.OPDS12ParseSucceeded
    val entry = success.result
    Assert.assertEquals("title", entry.title)
    Assert.assertEquals("id", entry.id)
    Assert.assertEquals(OPDS12Author("Author", null), entry.authors[0])
    Assert.assertEquals("Summary", entry.summary)
    Assert.assertEquals(OPDS12Category("http://schema.org/audience", "AdultTerm", "AdultLabel"), entry.categories[0])
    Assert.assertEquals(Instant.parse("2000-01-01T00:00:01Z"), entry.published)
    Assert.assertEquals(Instant.parse("2000-01-01T00:00:00Z"), entry.updated)

    run {
      val acquisition = entry.acquisitions[0]
      Assert.assertEquals(MIMEParser.parseRaisingException("application/atom+xml;type=entry;profile=opds-catalog"), acquisition.type)
      Assert.assertEquals(OPDS12AcquisitionRelation.ACQUISITION_BORROW, acquisition.relation)
      Assert.assertEquals(URI.create("http://example.com/borrow"), acquisition.uri)
    }
  }

  @Test
  fun testEntryAcquisitionMissingHref() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-acq-missing-href.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(2, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Missing a required attribute"))
    }
  }

  @Test
  fun testEntryAcquisitionBadType0() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-acq-bad-type-0.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(2, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Malformed MIME type"))
    }
  }

  @Test
  fun testEntryAcquisitionBadHref0() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-acq-bad-href-0.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(2, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Malformed URI"))
    }
  }

  @Test
  fun testEntryBadIndirect0() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-acq-bad-indirect-0.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(1, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Missing a required attribute"))
    }
  }

  @Test
  fun testEntryBadPublished() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-bad-published.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(1, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Malformed time value"))
    }
  }

  @Test
  fun testEntryBadUpdated() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-bad-updated.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(1, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Malformed time value"))
    }
  }

  @Test
  fun testEntryBadCoverHref() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-bad-cover.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(1, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Malformed URI"))
    }
  }

  @Test
  fun testEntryBadCategory() {
    val parser =
      this.parsers.createParser(
        uri = URI.create("urn:test"),
        stream = this.resource("entry-bad-category.xml"))

    val result = parser.parse()
    this.dumpParseResult(result)
    val failure = result as OPDS12ParseResult.OPDS12ParseFailed
    Assert.assertEquals(3, failure.errors.size)

    this.run {
      val error = result.errors[0]
      Assert.assertThat(error.message, StringContains.containsString("Missing a required attribute"))
    }
    this.run {
      val error = result.errors[1]
      Assert.assertThat(error.message, StringContains.containsString("Missing a required attribute"))
    }
    this.run {
      val error = result.errors[2]
      Assert.assertThat(error.message, StringContains.containsString("Missing a required attribute"))
    }
  }

  class FailingExtensionParser : OPDS12FeedEntryExtensionParserType {
    override fun parse(): OPDS12ParseResult<List<OPDS12ExtensionValueType>> {
      return OPDS12ParseResult.OPDS12ParseFailed(errors = listOf(
        OPDS12ParseResult.OPDS12ParseError(
          producer = "failing",
          lexical = OPDS12LexicalPosition(URI.create("x"), 24, 23),
          message = "Failed!",
          exception = null)))
    }
  }

  class FailingExtensionParserProvider : OPDS12FeedEntryExtensionParserProviderType {
    override fun createParser(context: OPDS12FeedEntryExtensionParserContextType): OPDS12FeedEntryExtensionParserType {
      return FailingExtensionParser()
    }
  }
}
