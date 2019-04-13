package one.irradia.opds1_2.api

import one.irradia.opds1_2.lexical.OPDS12LexicalPosition

/**
 * The result of parsing a document.
 */

sealed class OPDS12ParseResult<T> {

  companion object {

    /**
     * Monadic bind for results.
     */

    fun <A, B> flatMap(r: OPDS12ParseResult<A>, f: (A) -> OPDS12ParseResult<B>): OPDS12ParseResult<B> =
      when (r) {
        is OPDS12ParseSucceeded -> {
          val u = f.invoke(r.result)
          when (u) {
            is OPDS12ParseSucceeded ->
              OPDS12ParseSucceeded(warnings = r.warnings.plus(u.warnings), result = u.result)
            is OPDS12ParseFailed ->
              OPDS12ParseFailed(warnings = r.warnings.plus(u.warnings), errors = u.errors)
          }
        }
        is OPDS12ParseFailed ->
          OPDS12ParseFailed(warnings = r.warnings, errors = r.errors)
      }
  }

  /**
   * Monadic bind for results.
   */

  abstract fun <U> flatMap(
    f: (T) -> OPDS12ParseResult<U>): OPDS12ParseResult<U>

  /**
   * Parsing succeeded.
   */

  data class OPDS12ParseSucceeded<T>(

    /**
     * The warnings encountered.
     */

    val warnings: List<OPDS12ParseWarning> = listOf(),

    /**
     * The parsed value.
     */

    val result: T) : OPDS12ParseResult<T>() {

    override fun <U> flatMap(f: (T) -> OPDS12ParseResult<U>): OPDS12ParseResult<U> =
      Companion.flatMap(this, f)
  }

  /**
   * Parsing failed.
   */

  data class OPDS12ParseFailed<T>(

    /**
     * The warnings encountered.
     */

    val warnings: List<OPDS12ParseWarning> = listOf(),

    /**
     * The list of parse errors.
     */

    val errors: List<OPDS12ParseError>) : OPDS12ParseResult<T>() {

    override fun <U> flatMap(f: (T) -> OPDS12ParseResult<U>): OPDS12ParseResult<U> =
      Companion.flatMap(this, f)
  }

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

  /**
   * A specific parse warning.
   */

  data class OPDS12ParseWarning(

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
}
