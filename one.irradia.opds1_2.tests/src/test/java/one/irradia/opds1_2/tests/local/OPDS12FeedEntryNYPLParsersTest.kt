package one.irradia.opds1_2.tests.local

import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.vanilla.OPDS12FeedEntryParsers
import one.irradia.opds1_2.tests.OPDS12FeedEntryNYPLParserProviderContract
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OPDS12FeedEntryNYPLParsersTest : OPDS12FeedEntryNYPLParserProviderContract() {

  override fun logger(): Logger {
    return LoggerFactory.getLogger(OPDS12FeedEntryNYPLParsersTest::class.java)
  }

  override fun parsers(): OPDS12FeedEntryParserProviderType {
    return OPDS12FeedEntryParsers()
  }

}
