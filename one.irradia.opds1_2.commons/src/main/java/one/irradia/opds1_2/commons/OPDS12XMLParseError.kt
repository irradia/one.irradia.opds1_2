package one.irradia.opds1_2.commons

import one.irradia.opds1_2.lexical.OPDS12LexicalPosition

/**
 * A specific parse error.
 */

data class OPDS12XMLParseError(

  /**
   * The parser that produced the error. This will typically be the name of the core parser, or
   * the name of one of the extension parsers.
   */

  val producer: String,

  /**
   * Lexical information for the parse error.
   */

  val lexical: OPDS12LexicalPosition,

  /**
   * The error message.
   */

  val message: String,

  /**
   * The exception raised, if any.
   */

  val exception: Exception?)
