package one.irradia.opds1_2.tests.device

import android.support.test.filters.MediumTest
import android.support.test.runner.AndroidJUnit4
import one.irradia.opds1_2.parser.api.OPDS12AcquisitionFeedEntryParserProviderType
import one.irradia.opds1_2.parser.vanilla.OPDS12AcquisitionFeedEntryParsers
import one.irradia.opds1_2.tests.OPDS12AcquisitionFeedEntryDublinParserProviderContract
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RunWith(AndroidJUnit4::class)
@MediumTest
class OPDS12AcquisitionFeedEntryDublinParsersTest : OPDS12AcquisitionFeedEntryDublinParserProviderContract() {

  override fun logger(): Logger {
    return LoggerFactory.getLogger(OPDS12AcquisitionFeedEntryDublinParsersTest::class.java)
  }

  override fun parsers(): OPDS12AcquisitionFeedEntryParserProviderType {
    return OPDS12AcquisitionFeedEntryParsers()
  }

}
