package one.irradia.opds1_2.tests

import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import java.util.ServiceLoader

abstract class ServicesContract {

  abstract fun logger(): Logger

  @Test
  fun testOPDS12AcquisitionFeedEntryParserProviderType() {
    val loader =
      ServiceLoader.load(OPDS12FeedEntryParserProviderType::class.java)
    val services =
      loader.iterator().asSequence().toList()

    val logger = this.logger()
    services.forEach { service -> logger.debug("service: {}", service) }
    Assert.assertTrue("At least one service exists", services.size >= 1)
  }

  @Test
  fun testExtensionParsers0() {
    val loader =
      ServiceLoader.load(OPDS12FeedEntryExtensionParserProviderType::class.java)
    val services =
      loader.iterator().asSequence().toList()

    val logger = this.logger()
    services.forEach { service -> logger.debug("service: {}", service) }
    Assert.assertTrue("At least two services exists", services.size >= 2)
  }

}