package one.irradia.opds1_2.tests.device

import android.support.test.filters.MediumTest
import android.support.test.runner.AndroidJUnit4
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.vanilla.OPDS12FeedEntryParsers
import one.irradia.opds1_2.tests.OPDS12FeedEntryNYPLParserProviderContract
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RunWith(AndroidJUnit4::class)
@MediumTest
class OPDS12FeedEntryNYPLParsersTest : OPDS12FeedEntryNYPLParserProviderContract() {

  override fun logger(): Logger {
    return LoggerFactory.getLogger(OPDS12FeedEntryNYPLParsersTest::class.java)
  }

  override fun parsers(): OPDS12FeedEntryParserProviderType {
    return OPDS12FeedEntryParsers()
  }

}
