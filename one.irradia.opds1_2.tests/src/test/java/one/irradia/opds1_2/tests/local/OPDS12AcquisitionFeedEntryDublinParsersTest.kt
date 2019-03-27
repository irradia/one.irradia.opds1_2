package one.irradia.opds1_2.tests.local

import one.irradia.opds1_2.parser.api.OPDS12AcquisitionFeedEntryParserProviderType
import one.irradia.opds1_2.parser.vanilla.OPDS12AcquisitionFeedEntryParsers
import one.irradia.opds1_2.tests.OPDS12AcquisitionFeedEntryDublinParserProviderContract
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OPDS12AcquisitionFeedEntryDublinParsersTest : OPDS12AcquisitionFeedEntryDublinParserProviderContract() {

  override fun logger(): Logger {
    return LoggerFactory.getLogger(OPDS12AcquisitionFeedEntryDublinParsersTest::class.java)
  }

  override fun parsers(): OPDS12AcquisitionFeedEntryParserProviderType {
    return OPDS12AcquisitionFeedEntryParsers()
  }

}
