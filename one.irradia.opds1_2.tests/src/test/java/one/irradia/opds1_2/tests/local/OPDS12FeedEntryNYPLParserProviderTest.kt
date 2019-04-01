package one.irradia.opds1_2.tests.local

import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedParserProviderType
import one.irradia.opds1_2.parser.vanilla.OPDS12FeedEntryParsers
import one.irradia.opds1_2.parser.vanilla.OPDS12FeedParsers
import one.irradia.opds1_2.tests.OPDS12FeedParserNYPLProviderContract
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OPDS12FeedEntryNYPLParserProviderTest : OPDS12FeedParserNYPLProviderContract() {

  override fun entryParsers(): OPDS12FeedEntryParserProviderType {
    return OPDS12FeedEntryParsers()
  }

  override fun parsers(): OPDS12FeedParserProviderType {
    return OPDS12FeedParsers()
  }

  override fun logger(): Logger {
    return LoggerFactory.getLogger(OPDS12FeedParserProviderTest::class.java)
  }

}