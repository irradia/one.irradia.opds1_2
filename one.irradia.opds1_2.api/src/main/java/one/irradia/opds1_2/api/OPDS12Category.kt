package one.irradia.opds1_2.api

/**
 * An OPDS category.
 */

data class OPDS12Category(
  val scheme: String,
  val term: String,
  val label: String?): OPDS12ElementType
