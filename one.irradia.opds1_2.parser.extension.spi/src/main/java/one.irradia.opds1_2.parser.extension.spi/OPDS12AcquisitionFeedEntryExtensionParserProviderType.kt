package one.irradia.opds1_2.parser.extension.spi

/**
 * A provider of extension parsers.
 */

interface OPDS12AcquisitionFeedEntryExtensionParserProviderType {

  /**
   * @param context The parser context
   *
   * Create a parser.
   */

  fun createParser(
    context: OPDS12AcquisitionFeedEntryExtensionParserContextType)
    : OPDS12AcquisitionFeedEntryExtensionParserType

}
