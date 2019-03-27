package one.irradia.opds1_2.commons

import one.irradia.mime.api.MIMEType
import one.irradia.mime.vanilla.MIMEParser
import one.irradia.opds1_2.lexical.OPDS12LexicalPosition
import one.irradia.opds1_2.lexical.OPDS12LexicalPosition.Companion.XML_TREE_LEXICAL_KEY
import org.joda.time.Instant
import org.joda.time.format.ISODateTimeFormat
import org.w3c.dom.Element
import java.net.URI
import java.net.URISyntaxException

/**
 * A class for conveniently extracting data from XML.
 */

class OPDS12XMLProcessor(

  /**
   * The URI of the document being processed.
   */

  val currentDocument: URI,

  /**
   * The name of the producer currently using the processor.
   */

  val producer: String,

  /**
   * A function used to publish parse errors as they occur.
   */

  val errors: (OPDS12XMLParseError) -> Unit) {

  private val defaultLexical =
    OPDS12LexicalPosition(this.currentDocument, -1, -1)

  private fun obtainLexicalInfo(element: Element): one.irradia.opds1_2.lexical.OPDS12LexicalPosition {
    return (element.getUserData(XML_TREE_LEXICAL_KEY) as OPDS12LexicalPosition?)
      ?: defaultLexical
  }

  /**
   * Check that an element has an expected name.
   *
   * @return The element
   */

  fun requireElementIs(
    element: Element,
    namespace: URI,
    name: String): Element? {

    val namespaceReceived = element.namespaceURI
    return if (namespaceReceived != namespace.toString() || element.localName != name) {
      val lexical = this.obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Unexpected element
  Expected: An element $name in namespace '$namespace'
  Received: An element '${element.tagName}' in namespace '$namespaceReceived'
  Source:   ${lexical.source}:${lexical.line}:${lexical.column}
""".trimMargin(),
        exception = java.lang.Exception()))
      null
    } else {
      element
    }
  }

  /**
   * Find the first child element with the given name and namespace.
   *
   * @return The element
   */

  fun requireElement(
    element: Element,
    namespace: URI,
    name: String): Element? {

    val received =
      firstChildElementWithName(element, namespace, name)

    return if (received == null) {
      val lexical = this.obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Missing a required element
  Expected: An element '$name' in namespace '$namespace'
  Received: Nothing
  Source:   ${lexical.source}:${lexical.line}:${lexical.column}
        """.trimMargin(),
        exception = java.lang.Exception()))
      null
    } else {
      received
    }
  }

  /**
   * Retrieve the text content of the named element, or return an empty string if the element
   * does not exist.
   */

  fun optionalElementTextOrEmpty(
    element: Element,
    namespace: URI,
    name: String): String {
    return firstChildElementTextWithName(element, namespace, name) ?: ""
  }

  /**
   * Retrieve the text content of the named element, or return `null` if the element
   * does not exist.
   */

  fun optionalElementText(
    element: Element,
    namespace: URI,
    name: String): String? {
    return firstChildElementTextWithName(element, namespace, name)
  }

  /**
   * Find the first child element with the given name and namespace, and try to parse a timestamp
   * if it exists.
   *
   * @return The element
   */

  fun optionalElementInstant(
    element: Element,
    namespace: URI,
    name: String): Instant? {

    val received =
      firstChildElementWithName(element, namespace, name)

    return if (received == null) {
      null
    } else {
      try {
        Instant.parse(received.textContent, ISODateTimeFormat.dateTimeParser())
      } catch (e: IllegalArgumentException) {
        val lexical = this.obtainLexicalInfo(element)
        this.errors.invoke(OPDS12XMLParseError(
          producer = this.producer,
          lexical = lexical,
          message = """Malformed time value
  Expected: A valid time in element '${received.tagName}'
  Received: ${received.textContent}
  Source:   ${lexical.source}:${lexical.line}:${lexical.column}
            """.trimMargin(),
          exception = e))
        null
      }
    }
  }

  /**
   * If `element` has an attribute named `attributeName` with value `attributeValue`, then extract
   * the value of the attribute named `attributeExtract` and try to parse it as a URI.
   */

  fun optionalIndirectURIAttribute(
    element: Element,
    attributeName: String,
    attributeValue: String,
    attributeExtract: String): URI? {

    val received =
      hasAttributeWithValueThenExtract(
        element = element,
        attributeName = attributeName,
        attributeValue = attributeValue,
        extractAttribute = attributeExtract)

    return if (received == null) {
      null
    } else {
      try {
        URI(received)
      } catch (e: URISyntaxException) {
        val lexical = this.obtainLexicalInfo(element)
        this.errors.invoke(OPDS12XMLParseError(
          producer = this.producer,
          lexical = lexical,
          message = """Malformed URI in attribute
  Expected: A valid URI for attribute $attributeExtract in element '${element.tagName}' with '$attributeName'='$attributeValue'
  Received: $received
  Source:   ${lexical.source}:${lexical.line}:${lexical.column}
            """.trimMargin(),
          exception = e))
        null
      }
    }
  }

  /**
   * Find the first child element with the given name and namespace.
   *
   * @return The element
   */

  fun requireElementText(
    element: Element,
    namespace: URI,
    name: String): String? {
    return requireElement(element, namespace, name)?.textContent
  }

  /**
   * @return The value of the named attribute
   */

  fun requireAttribute(
    element: Element,
    name: String): String? {

    val received = element.getAttribute(name)
    return if (received == null || received.isEmpty()) {
      val lexical = this.obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Missing a required attribute
  Expected: An attribute '$name' on element '${element.tagName}' in namespace '${element.namespaceURI}'
  Received: Nothing
  Source:   ${lexical.source}:${lexical.line}:${lexical.column}
        """.trimMargin(),
        exception = java.lang.Exception()))
      null
    } else {
      received
    }
  }

  /**
   * @return The value of the named attribute
   */

  fun requireAttributeURI(
    element: Element,
    name: String): URI? {

    val text = requireAttribute(element, name)
    return try {
      if (text != null) {
        URI(text)
      } else {
        null
      }
    } catch (e: URISyntaxException) {
      val lexical = obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Malformed URI in attribute
    Expected: A valid URI for attribute $name in element '${element.tagName}'
    Received: $text
    Source:   ${lexical.source}:${lexical.line}:${lexical.column}
            """.trimMargin(),
        exception = e))
      null
    }
  }

  /**
   * @return The value of the named attribute
   */

  fun requireAttributeMIMEType(
    element: Element,
    name: String): MIMEType? {

    val text = requireAttribute(element, name)
    return try {
      if (text != null) {
        MIMEParser.parseRaisingException(text)
      } else {
        null
      }
    } catch (e: java.lang.Exception) {
      val lexical = obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Malformed URI in attribute
    Expected: A valid URI for attribute $name in element '${element.tagName}'
    Received: $text
    Source:   ${lexical.source}:${lexical.line}:${lexical.column}
            """.trimMargin(),
        exception = e))
      null
    }
  }

  /**
   * @return The value of the named attribute
   */

  fun optionalAttributeURI(
    element: Element,
    name: String): URI? {

    val text = optionalAttribute(element, name)
    return try {
      if (text != null) {
        URI(text)
      } else {
        null
      }
    } catch (e: URISyntaxException) {
      val lexical = obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Malformed URI in attribute
    Expected: A valid URI for attribute $name in element '${element.tagName}'
    Received: $text
    Source:   ${lexical.source}:${lexical.line}:${lexical.column}
            """.trimMargin(),
        exception = e))
      null
    }
  }

  /**
   * @return The value of the named attribute
   */

  fun optionalAttributeInt(
    element: Element,
    name: String): Int? {

    val text = optionalAttribute(element, name)
    return try {
      if (text != null) {
        Integer.parseInt(text)
      } else {
        null
      }
    } catch (e: NumberFormatException) {
      val lexical = obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Malformed integer in attribute
    Expected: A valid integer for attribute $name in element '${element.tagName}'
    Received: $text
    Source:   ${lexical.source}:${lexical.line}:${lexical.column}
            """.trimMargin(),
        exception = e))
      null
    }
  }

  /**
   * @return The value of the named attribute
   */

  fun optionalAttributeInstant(
    element: Element,
    name: String): Instant? {

    val text = optionalAttribute(element, name)
    return try {
      if (text != null) {
        Instant.parse(text, ISODateTimeFormat.dateTimeParser())
      } else {
        null
      }
    } catch (e: IllegalArgumentException) {
      val lexical = this.obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Malformed time value
  Expected: A valid time in element '${element.tagName}'
  Received: $text
  Source:   ${lexical.source}:${lexical.line}:${lexical.column}
            """.trimMargin(),
        exception = e))
      null
    }
  }

  /**
   * @return The value of the named attribute
   */

  fun optionalAttributeMIMEType(
    element: Element,
    name: String): MIMEType? {

    val text = optionalAttribute(element, name)
    return try {
      if (text != null) {
        MIMEParser.parseRaisingException(text)
      } else {
        null
      }
    } catch (e: Exception) {
      val lexical = this.obtainLexicalInfo(element)
      this.errors.invoke(OPDS12XMLParseError(
        producer = this.producer,
        lexical = lexical,
        message = """Malformed MIME type value
  Expected: A valid MIME type in element '${element.tagName}'
  Received: $text
  Source:   ${lexical.source}:${lexical.line}:${lexical.column}
            """.trimMargin(),
        exception = e))
      null
    }
  }

  /**
   * @return The value of the named attribute
   */

  fun optionalAttribute(element: Element, name: String): String? {
    val text = element.getAttribute(name)
    if (text != null && text.isEmpty()) {
      return null
    }
    return text
  }

  /**
   * @return All child elements of the given element with the given name and namespace
   */

  fun allChildElementsWithName(
    element: Element,
    namespace: URI,
    name: String): List<Element> {

    val results = mutableListOf<Element>()
    val elements = element.getElementsByTagNameNS(namespace.toString(), name)
    for (i in 0 until elements.length) {
      results.add(elements.item(i) as Element)
    }
    return results.toList()
  }

  /**
   * @return The first child element of the given element with the given name and namespace
   */

  fun firstChildElementWithName(
    element: Element,
    namespace: URI,
    name: String): Element? {

    val elements = allChildElementsWithName(element, namespace, name)
    return if (elements.isEmpty()) {
      null
    } else {
      elements[0]
    }
  }

  /**
   * @return The text of the first child element of the given element with the given name and namespace
   */

  fun firstChildElementTextWithName(
    element: Element,
    namespace: URI,
    name: String): String? {
    return firstChildElementWithName(element, namespace, name)?.textContent
  }

  /**
   * @return `true` if the given element has an attribute with the given value
   */

  fun hasAttributeWithValue(
    element: Element,
    attributeName: String,
    attributeValue: String): Boolean {
    return (element.getAttribute(attributeName) ?: "") == attributeValue
  }

  /**
   * @return The value of the attribute `extractAttribute` if `attributeName` exists and has value `attributeValue`
   */

  fun hasAttributeWithValueThenExtract(
    element: Element,
    attributeName: String,
    attributeValue: String,
    extractAttribute: String): String? {
    return if (hasAttributeWithValue(element, attributeName, attributeValue)) {
      element.getAttribute(extractAttribute)
    } else {
      null
    }
  }

  /**
   * @return The value of the attribute `name` or `null` if no attribute exists
   */

  fun attributeValue(element: Element, name: String): String? {
    val value = element.getAttribute(name)
    return if (value != null) {
      if (value.isEmpty()) null else value
    } else {
      null
    }
  }
}
