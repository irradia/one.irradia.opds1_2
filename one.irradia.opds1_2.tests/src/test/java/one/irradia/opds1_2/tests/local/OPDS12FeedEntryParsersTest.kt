package one.irradia.opds1_2.tests.local

import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.vanilla.OPDS12FeedEntryParsers
import one.irradia.opds1_2.tests.OPDS12FeedEntryParserProviderContract
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OPDS12FeedEntryParsersTest: OPDS12FeedEntryParserProviderContract() {

  override fun logger(): Logger {
    return LoggerFactory.getLogger(OPDS12FeedEntryParsersTest::class.java)
  }

  override fun parsers(): OPDS12FeedEntryParserProviderType {
    return OPDS12FeedEntryParsers()
  }

}
