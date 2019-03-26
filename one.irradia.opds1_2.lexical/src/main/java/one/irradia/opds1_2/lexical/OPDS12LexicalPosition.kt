package one.irradia.opds1_2.lexical

import java.net.URI

data class OPDS12LexicalPosition(
  val source: URI,
  val line: Int,
  val column: Int) {

  companion object {

    /**
     * The key used to store lexical user data in XML trees.
     */

    const val XML_TREE_LEXICAL_KEY = "one.irradia.opds1_2.lexical"
  }
}
