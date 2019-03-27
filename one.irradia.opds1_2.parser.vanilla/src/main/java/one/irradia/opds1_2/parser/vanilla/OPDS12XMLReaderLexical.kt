package one.irradia.opds1_2.parser.vanilla

import com.google.common.base.Preconditions
import one.irradia.opds1_2.lexical.OPDS12LexicalPosition.Companion.XML_TREE_LEXICAL_KEY
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.Attributes
import org.xml.sax.Locator
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.ArrayDeque
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.SAXParserFactory

/**
 * A parser that produces documents that contain lexical information taken from the
 * underlying SAX parser.
 */

object OPDS12XMLReaderLexical {

  /**
   * Read an XML file from the given stream, parsing it and inserting lexical information
   * into each element.
   */

  @JvmStatic
  @Throws(IOException::class, SAXException::class)
  fun readXML(source: URI, inputStream: InputStream): Document {

    val factory = SAXParserFactory.newInstance()
    factory.isNamespaceAware = true
    factory.isValidating = false

    val parser = factory.newSAXParser()
    Preconditions.checkArgument(parser.isNamespaceAware, "Parser must be namespace aware")
    Preconditions.checkArgument(!parser.isValidating, "Parser must not be validating")

    val docBuilderFactory = DocumentBuilderFactory.newInstance()
    docBuilderFactory.isNamespaceAware = true
    docBuilderFactory.isValidating = false

    val docBuilder = docBuilderFactory.newDocumentBuilder()
    Preconditions.checkArgument(docBuilder.isNamespaceAware, "Document builder must be namespace aware")
    Preconditions.checkArgument(!docBuilder.isValidating, "Document builder must not be validating")

    val document = docBuilder.newDocument()
    val elementStack = ArrayDeque<Element>()
    val textBuffer = StringBuilder()
    val handler = LexicalHandler(document, source, elementStack, textBuffer)
    parser.parse(inputStream, handler)
    return document
  }

  internal class LexicalHandler(
    private val document: Document,
    private val source: URI,
    private val elementStack: ArrayDeque<Element>,
    private val textBuffer: StringBuilder
  ) : DefaultHandler() {
    private lateinit var locator: Locator

    override fun setDocumentLocator(locator: Locator) {
      this.locator = locator
    }

    @Throws(SAXException::class)
    override fun startElement(
      uri: String,
      localName: String,
      qName: String,
      attributes: Attributes) {

      this.addTextIfNeeded()
      val element = this.document.createElementNS(uri, qName)
      for (i in 0 until attributes.length) {
        element.setAttribute(attributes.getQName(i), attributes.getValue(i))
      }

      element.setUserData(
        XML_TREE_LEXICAL_KEY,
        one.irradia.opds1_2.lexical.OPDS12LexicalPosition(
          source = this.source,
          line = this.locator.lineNumber,
          column = this.locator.columnNumber),
        null)
      this.elementStack.push(element)
    }

    override fun endElement(
      uri: String,
      localName: String,
      qName: String) {

      this.addTextIfNeeded()
      val closedElement = this.elementStack.pop()
      if (this.elementStack.isEmpty()) {
        this.document.appendChild(closedElement)
      } else {
        val parentElement = this.elementStack.peek()
        parentElement.appendChild(closedElement)
      }
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
      this.textBuffer.append(ch, start, length)
    }

    private fun addTextIfNeeded() {
      if (this.textBuffer.isNotEmpty()) {
        val element = this.elementStack.peek()
        val textNode = this.document.createTextNode(this.textBuffer.toString())
        element.appendChild(textNode)
        this.textBuffer.delete(0, this.textBuffer.length)
      }
    }
  }
}