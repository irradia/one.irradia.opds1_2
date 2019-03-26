package one.irradia.opds1_2.tests.local

import one.irradia.opds1_2.parser.api.OPDS12AcquisitionFeedEntryParserProviderType
import one.irradia.opds1_2.parser.vanilla.OPDS12AcquisitionFeedEntryParsers
import one.irradia.opds1_2.tests.OPDS12AcquisitionFeedEntryParserProviderContract
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OPDS12AcquisitionFeedEntryParsersTest: OPDS12AcquisitionFeedEntryParserProviderContract() {

  override fun logger(): Logger {
    return LoggerFactory.getLogger(OPDS12AcquisitionFeedEntryParsersTest::class.java)
  }

  override fun parsers(): OPDS12AcquisitionFeedEntryParserProviderType {
    return OPDS12AcquisitionFeedEntryParsers()
  }

}
