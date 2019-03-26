package one.irradia.opds1_2.parser.extension.spi

/**
 * A provider of extension parsers.
 */

interface OPDS12AcquisitionFeedEntryExtensionParserProviderType {

  /**
   * @param documentURI The URI of the owning document
   * @param entry The parsed entry minus any extensions
   * @param elementMap The elements that were used to construct a particular feed element
   * @param element The raw parsed XML element
   *
   * Create a parser.
   */

  fun createParser(
    context: OPDS12AcquisitionFeedEntryExtensionParserContextType)
    : OPDS12AcquisitionFeedEntryExtensionParserType

}
