sdp
=================

[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/crow-misia/sdp/build.yml)](https://github.com/crow-misia/sdp/actions/workflows/build.yml)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.crow-misia.sdp/sdp)](https://central.sonatype.com/artifact/io.github.crow-misia.sdp/sdp)
[![javadoc](https://javadoc.io/badge2/io.github.crow-misia.sdp/sdp/javadoc.svg)](https://javadoc.io/doc/io.github.crow-misia.sdp/sdp)
[![GitHub License](https://img.shields.io/github/license/crow-misia/sdp)](LICENSE)

SDP(Session Description Protocol) parse / serialization

## Get Started

### Gradle

Add dependencies (you can also add other modules that you need):

`${latest.version}` is [![Maven Central Version](https://img.shields.io/maven-central/v/io.github.crow-misia.sdp/sdp)](https://central.sonatype.com/artifact/io.github.crow-misia.sdp/sdp)

```groovy
dependencies {
    implementation "io.github.crow-misia.sdp:sdp:${latest.version}"
}
```

Make sure that you have either `mavenCentral()` in the list of repositories:

```
repository {
    mavenCentral()
}
```


## Reference

* RFC5245: Interactive Connectivity Establishment (ICE): A Protocol for Network Address Translator (NAT) Traversal for Offer/Answer Protocols
  https://datatracker.ietf.org/doc/html/rfc5245
* RFC5761: Multiplexing RTP Data and Control Packets on a Single Port
  https://datatracker.ietf.org/doc/html/rfc5761
* RFC8841: Session Description Protocol (SDP) Offer/Answer Procedures for Stream Control Transmission Protocol (SCTP) over Datagram Transport Layer Security (DTLS) Transport
  https://datatracker.ietf.org/doc/html/rfc8841
* RFC8866: SDP: Session Description Protocol
  https://datatracker.ietf.org/doc/html/rfc8866
