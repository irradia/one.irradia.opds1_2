package one.irradia.opds1_2.api

/**
 * An OPDS category.
 */

data class OPDS12Category(

  /**
   * The categorization scheme
   */

  val scheme: String,

  /**
   * The category
   */

  val term: String,

  /**
   * A human-readable label for display
   */

  val label: String): OPDS12ElementType
