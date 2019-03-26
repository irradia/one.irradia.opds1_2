package one.irradia.opds1_2.api

import java.net.URI

object OPDS12Identifiers {

  val ATOM_URI =
    URI.create("http://www.w3.org/2005/Atom")

  val BIBFRAME_URI =
    URI.create("http://bibframe.org/vocab/")

  val DUBLIN_CORE_TERMS_URI =
    URI.create("http://purl.org/dc/terms/")

  val ACQUISITION_URI_PREFIX =
    URI.create("http://opds-spec.org/acquisition")

  val FACET_URI =
    URI.create("http://opds-spec.org/facet")

  val GROUP_REL_TEXT =
    "collection"

  val OPDS_URI =
    URI.create("http://opds-spec.org/2010/catalog")

  val THUMBNAIL_URI =
    URI.create("http://opds-spec.org/image/thumbnail")

  val ISSUES_REL_TEXT =
    "issues"

  val ALTERNATE_REL_TEXT =
    "alternate"

  val RELATED_REL_TEXT =
    "related"

  val IMAGE_URI =
    URI.create("http://opds-spec.org/image")

  val SCHEMA_URI =
    URI.create("http://schema.org/")

  val ANNOTATION_URI =
    URI.create("http://www.w3.org/ns/oa#annotationService")

}
