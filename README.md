one.irradia.opds1_2
===

[![Build Status](https://img.shields.io/travis/irradia/one.irradia.opds1_2.svg?style=flat-square)](https://travis-ci.org/irradia/one.irradia.opds1_2)
[![Maven Central](https://img.shields.io/maven-central/v/one.irradia.opds1_2/one.irradia.opds1_2.api.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22one.irradia.opds1_2%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/oss.sonatype.org/one.irradia.opds1_2/one.irradia.opds1_2.api.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/one.irradia.opds1_2/)
[![Codacy Badge](https://img.shields.io/codacy/grade/616a69d629824b299983d4c2f673e84b.svg?style=flat-square)](https://www.codacy.com/app/github_79/one.irradia.opds1_2?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=irradia/one.irradia.opds1_2&amp;utm_campaign=Badge_Grade)
[![Codecov](https://img.shields.io/codecov/c/github/irradia/one.irradia.opds1_2.svg?style=flat-square)](https://codecov.io/gh/irradia/one.irradia.opds1_2)
[![Gitter](https://badges.gitter.im/irradia-org/community.svg)](https://gitter.im/irradia-org/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

![opds1_2](./src/site/resources/opds1_2.jpg?raw=true)

## Building

Install the Android SDK.

```
$ ./gradlew clean assembleDebug test
```

If the above fails, it's a bug. Report it!

## Using

Use the following Maven or Gradle dependencies, replacing `${LATEST_VERSION_HERE}` with
whatever is the latest version published to Maven Central:

```
<!-- API -->
<dependency>
  <groupId>one.irradia.opds1_2</groupId>
  <artifactId>one.irradia.opds1_2.api</artifactId>
  <version>${LATEST_VERSION_HERE}</version>
</dependency>

<!-- Parser API -->
<dependency>
  <groupId>one.irradia.opds1_2</groupId>
  <artifactId>one.irradia.opds1_2.parser.api</artifactId>
  <version>${LATEST_VERSION_HERE}</version>
</dependency>

<!-- Default implementation -->
<dependency>
  <groupId>one.irradia.opds1_2</groupId>
  <artifactId>one.irradia.opds1_2.parser.vanilla</artifactId>
  <version>${LATEST_VERSION_HERE}</version>
</dependency>
```

```
repositories {
  mavenCentral()
}

implementation "one.irradia.opds1_2:one.irradia.opds1_2.api:${LATEST_VERSION_HERE}"
implementation "one.irradia.opds1_2:one.irradia.opds1_2.parser.api:${LATEST_VERSION_HERE}"
implementation "one.irradia.opds1_2:one.irradia.opds1_2.parser.vanilla:${LATEST_VERSION_HERE}"
```

Library code is encouraged to depend only upon the API package in order to give consumers
the freedom to use other implementations of the API if desired.

## Modules

|Module|Description|
|------|-----------|
| [one.irradia.opds1_2.api](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.api) | Core API
| [one.irradia.opds1_2.commons](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.commons) | Common code shared between implementations
| [one.irradia.opds1_2.dublin](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.dublin) | Dublin Core extensions
| [one.irradia.opds1_2.lexical](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.lexical) | Lexical types used by parsers
| [one.irradia.opds1_2.nypl](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.nypl) | Functionality specific to NYPL OPDS feeds
| [one.irradia.opds1_2.parser.api](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.parser.api) | Parser API
| [one.irradia.opds1_2.parser.extension.spi](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.parser.extension.spi) | Parser extension SPI
| [one.irradia.opds1_2.tests.device](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.tests.device) | Unit tests that execute on real or emulated devices
| [one.irradia.opds1_2.tests](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.tests) | Unit tests that can execute without needing a real or emulated device
| [one.irradia.opds1_2.vanilla](https://github.com/irradia/one.irradia.opds1_2/tree/develop/one.irradia.opds1_2.vanilla) | Vanilla parser implementation

## Publishing Releases

Releases are published to Maven Central with the following invocation:

```
$ ./gradlew clean assembleDebug publish closeAndReleaseRepository
```

Consult the documentation for the [Gradle Signing plugin](https://docs.gradle.org/current/userguide/signing_plugin.html)
and the [Gradle Nexus staging plugin](https://github.com/Codearte/gradle-nexus-staging-plugin/) for
details on what needs to go into your `~/.gradle/gradle.properties` file to do the appropriate
PGP signing of artifacts and uploads to Maven Central.

## Semantic Versioning

All [irradia.one](https://www.irradia.one) packages obey [Semantic Versioning](https://www.semver.org)
once they reach version `1.0.0`.
