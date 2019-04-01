package one.irradia.opds1_2.lexical

import java.net.URI

/**
 * A lexical position of an element in a document.
 */

data class OPDS12LexicalPosition(

  /**
   * The source URI of the document.
   */

  val source: URI,

  /**
   * The line number.
   */

  val line: Int,

  /**
   * The column number.
   */

  val column: Int) {

  companion object {

    /**
     * The key used to store lexical user data in XML trees.
     */

    const val XML_TREE_LEXICAL_KEY = "one.irradia.opds1_2.lexical"
  }
}
