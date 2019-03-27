package one.irradia.opds1_2.dublin

import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserContextType
import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12AcquisitionFeedEntryExtensionParserType

/**
 * A parser for Dublin Core extensions.
 *
 * Note: This class must have a no-argument public constructor to work with [java.util.ServiceLoader].
 */

class OPDS12DublinAcquisitionFeedEntryParsers : OPDS12AcquisitionFeedEntryExtensionParserProviderType {

  override fun createParser(
    context: OPDS12AcquisitionFeedEntryExtensionParserContextType)
    : OPDS12AcquisitionFeedEntryExtensionParserType {
    return OPDS12DublinAcquisitionFeedEntryParser(context)
  }

}
