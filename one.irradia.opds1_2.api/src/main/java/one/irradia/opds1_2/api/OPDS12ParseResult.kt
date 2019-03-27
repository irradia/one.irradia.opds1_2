package one.irradia.opds1_2.api

import one.irradia.opds1_2.lexical.OPDS12LexicalPosition

/**
 * The result of parsing a document.
 */

sealed class OPDS12ParseResult<T> {

  /**
   * Parsing succeeded.
   */

  data class OPDS12ParseSucceeded<T>(

    /**
     * The parsed value.
     */

    val result: T) : OPDS12ParseResult<T>()

  /**
   * Parsing failed.
   */

  data class OPDS12ParseFailed<T>(

    /**
     * The list of parse errors.
     */

    val errors: List<OPDS12ParseError>) : OPDS12ParseResult<T>()

  /**
   * A specific parse error.
   */

  data class OPDS12ParseError(

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

}
