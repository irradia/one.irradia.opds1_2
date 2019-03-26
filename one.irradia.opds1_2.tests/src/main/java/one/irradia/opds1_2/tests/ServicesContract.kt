package one.irradia.opds1_2.tests

import one.irradia.opds1_2.parser.api.OPDS12AcquisitionFeedEntryParserProviderType
import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import java.util.ServiceLoader

abstract class ServicesContract {

  abstract fun logger(): Logger

  @Test
  fun testOPDS12AcquisitionFeedEntryParserProviderType() {
    val loader =
      ServiceLoader.load(OPDS12AcquisitionFeedEntryParserProviderType::class.java)
    val services =
      loader.iterator().asSequence().toList()

    val logger = this.logger()
    services.forEach { service -> logger.debug("service: {}", service) }
    Assert.assertTrue("At least one service exists", services.size > 0)
  }

}