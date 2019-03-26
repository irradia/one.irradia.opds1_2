package one.irradia.opds1_2.commons

import one.irradia.opds1_2.lexical.OPDS12LexicalPosition

data class OPDS12XMLParseError(
  val producer: String,
  val lexical: OPDS12LexicalPosition,
  val message: String,
  val exception: Exception?)
