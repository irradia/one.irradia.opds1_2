package one.irradia.opds1_2.api

import one.irradia.opds1_2.lexical.OPDS12LexicalPosition

sealed class OPDS12ParseResult<T> {

  data class OPDS12ParseSucceeded<T>(
    val result: T) : OPDS12ParseResult<T>()

  data class OPDS12ParseFailed<T>(
    val errors: List<OPDS12ParseError>) : OPDS12ParseResult<T>()

  data class OPDS12ParseError(
    val producer: String,
    val lexical: OPDS12LexicalPosition,
    val message: String,
    val exception: Exception?)

}
