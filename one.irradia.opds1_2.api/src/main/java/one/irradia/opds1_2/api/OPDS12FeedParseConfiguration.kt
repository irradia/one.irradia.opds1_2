package one.irradia.opds1_2.api

/**
 * Configuration values for parsers.
 */

data class OPDS12FeedParseConfiguration(

  /**
   * In contexts that allow for optional URI values, treat invalid URIs as if they had simply
   * not been specified at all.
   */

  val allowInvalidURIs: Boolean = false,

  /**
   * In contexts that allow for optional timestamp values, treat invalid timestamps as if they
   * had simply not been specified at all.
   */

  val allowInvalidTimestamps: Boolean = false,

  /**
   * In contexts that allow for optional MIME type values, treat invalid values as if they
   * had simply not been specified at all.
   */

  val allowInvalidMIMETypes: Boolean = false,

  /**
   * In contexts that allow for optional integer values, treat invalid values as if they
   * had simply not been specified at all.
   */

  val allowInvalidIntegers: Boolean = false)
