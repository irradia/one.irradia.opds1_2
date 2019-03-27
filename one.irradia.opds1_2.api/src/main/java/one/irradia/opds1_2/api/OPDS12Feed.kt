package one.irradia.opds1_2.api

import com.google.common.base.Preconditions
import org.joda.time.Instant
import java.net.URI

/**
 * An OPDS feed.
 */

data class OPDS12Feed(

  /**
   * The feed ID.
   */

  val id: String,

  /**
   * The feed title.
   */

  val title: String,

  /**
   * The time of last update for the feed.
   */

  val updated: Instant?,

  /**
   * The time the feed was published.
   */

  val published: Instant?,

  /**
   * The URI of the feed.
   */

  val uri: URI,

  /**
   * The base URI of the feed, used to resolve relative URIs. Note that this may be different
   * to the `uri` field, because `uri` specifies the location of the feed document where as `baseURI`
   * can be overridden by documents using `xml:base` attributes.
   */

  val baseURI: URI = uri,

  /**
   * The extension values included in the feed.
   */

  val extensions: List<OPDS12ExtensionValueType>,

  /**
   * The links present in the feed.
   */

  val links: List<OPDS12Link>,

  /**
   * The list of entries.
   */

  val entries: List<OPDS12FeedEntry>,

  /**
   * The facets in declaration order.
   */

  val facetsInOrder: List<OPDS12Facet>,

  /**
   * The groups in declaration order.
   */

  val groupsInOrder: List<OPDS12Group>) : OPDS12ElementType {

  /**
   * The facets grouped by name.
   */

  val facetsByGroup: Map<String, List<OPDS12Facet>> = mapOf()

  /**
   * The available groups by name.
   */

  val groups: Map<String, OPDS12Group> = mapOf()

  /**
   * `true` if this feed is an acquisition feed
   */

  val isAcquisitionFeed =
    this.entries.isEmpty() || this.entries.any { entry -> !entry.acquisitions.isEmpty() }

  /**
   * The links to search documents, if any
   */

  val searchLinks : List<OPDS12Link> =
    this.links.filter { link -> link.relation == "search" }

  /**
   * The link to the next part of this feed, if any
   */

  val next: OPDS12Link? =
    this.links.find { link -> link.relation == "next" }

  /**
   * The link to authentication document for the feed, if any
   */

  val authenticationDocument: OPDS12Link? =
    this.links.find { link -> link.relation == "http://opds-spec.org/auth/document" }

  /**
   * The link to "about" document for the feed, if any
   */

  val about: OPDS12Link? =
    this.links.find { link -> link.relation == "about" }

  /**
   * The link to the "terms of service" document for the feed, if any
   */

  val termsOfService: OPDS12Link? =
    this.links.find { link -> link.relation == "terms-of-service" }

  /**
   * The link to the "terms of service" document for the feed, if any
   */

  val privacyPolicy: OPDS12Link? =
    this.links.find { link -> link.relation == "privacyPolicy" }

  /**
   * The link to the "license" document for the feed, if any
   */

  val license: OPDS12Link? =
    this.links.find { link -> link.relation == "license" }

  /**
   * The link to the "copyright" document for the feed, if any
   */

  val copyright: OPDS12Link? =
    this.links.find { link -> link.relation == "copyright" }

  /**
   * The link to the start of the feed.
   */

  val start: OPDS12Link? =
    this.links.find { link -> link.relation == "start" }

  /**
   * The link to the reader's shelf if one is provided.
   */

  val shelf: OPDS12Link? =
    this.links.find { link -> link.relation == "http://opds-spec.org/shelf" }

  init {
    Preconditions.checkArgument(!this.id.isEmpty(), "Feed IDs cannot be empty")
  }
}