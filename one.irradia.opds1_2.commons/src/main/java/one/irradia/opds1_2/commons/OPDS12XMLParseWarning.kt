package one.irradia.opds1_2.commons

import one.irradia.opds1_2.lexical.OPDS12LexicalPosition

/**
 * A specific parse warning.
 */

data class OPDS12XMLParseWarning(

  /**
   * The parser that produced the warning. This will typically be the name of the core parser, or
   * the name of one of the extension parsers.
   */

  val producer: String,

  /**
   * Lexical information for the parse warning.
   */

  val lexical: OPDS12LexicalPosition,

  /**
   * The warning message.
   */

  val message: String,

  /**
   * The exception raised, if any.
   */

  val exception: Exception?)
