package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.CandidateAttribute
import io.github.crow_misia.sdp.attribute.ExtMapAttribute
import io.github.crow_misia.sdp.attribute.RTCPAttribute
import io.github.crow_misia.sdp.attribute.SourceFilterAttribute
import io.github.crow_misia.sdp.attribute.SsrcGroupAttribute
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

/**
 * RFC 4566/4570 ABNF requires exactly one `SP` between fields, but real-world
 * payloads sometimes emit runs of spaces. Under the opt-in lenient policy
 * ([LenientSdpContext], selected by `SdpSessionDescription.parse(text, strict =
 * false)`) parsing collapses those runs instead of producing empty tokens that
 * would otherwise misalign positional fields or fail the arity check.
 *
 * Strict (the default) behavior is covered by [AbnfStrictWhitespaceTest].
 */
class AbnfLenientWhitespaceTest : StringSpec({
    "source-filter with a double space keeps fields aligned (RFC 4570 regression)" {
        // a=source-filter: incl IN IP4  239.160.25.253 0.0.0.0  (two spaces after IP4)
        val actual = with(LenientSdpContext) { SourceFilterAttribute.parse(" incl IN IP4  239.1.1.1 0.0.0.0") }
        actual shouldBe SourceFilterAttribute.of("incl", "IN", "IP4", "239.1.1.1", "0.0.0.0")
    }

    "source-filter with extra spaces parses equal to the single-space form" {
        with(LenientSdpContext) {
            SourceFilterAttribute.parse("incl   IN  IP4   239.5.2.31    10.1.15.5") shouldBe
                SourceFilterAttribute.parse("incl IN IP4 239.5.2.31 10.1.15.5")
        }
    }

    "origin (o=) tolerates extra spaces" {
        with(LenientSdpContext) {
            SdpOrigin.parse("o=jdoe 2890844526  2890842807   IN IP4  10.47.16.5") shouldBe
                SdpOrigin.parse("o=jdoe 2890844526 2890842807 IN IP4 10.47.16.5")
        }
    }

    "connection (c=) tolerates extra spaces" {
        with(LenientSdpContext) {
            SdpConnection.parse("c=IN  IP4   224.2.17.12/127") shouldBe
                SdpConnection.parse("c=IN IP4 224.2.17.12/127")
        }
    }

    "timing (t=) tolerates extra spaces" {
        with(LenientSdpContext) {
            SdpTimeActive.parse("t=2873397496   2873404696") shouldBe
                SdpTimeActive.parse("t=2873397496 2873404696")
        }
    }

    "rtcp attribute tolerates extra spaces" {
        with(LenientSdpContext) {
            RTCPAttribute.parse("65179  IN   IP4  123.45.67.89") shouldBe
                RTCPAttribute.parse("65179 IN IP4 123.45.67.89")
        }
    }

    "extmap attribute tolerates extra spaces between fields" {
        with(LenientSdpContext) {
            ExtMapAttribute.parse("3  urn:ietf:params:rtp-hdrext:encrypt   urn:x  25@600/24") shouldBe
                ExtMapAttribute.parse("3 urn:ietf:params:rtp-hdrext:encrypt urn:x 25@600/24")
        }
    }

    "ssrc-group collapses extra spaces in the ssrc list" {
        val actual = with(LenientSdpContext) { SsrcGroupAttribute.parse("FID 123  456   789") } as SsrcGroupAttribute
        actual.semantics shouldBe "FID"
        actual.ssrcs shouldContainExactly listOf(123L, 456L, 789L)
    }

    "candidate attribute tolerates extra spaces" {
        with(LenientSdpContext) {
            CandidateAttribute.parse("0 1 UDP 2113667327  203.0.113.1  54400 typ host") shouldBe
                CandidateAttribute.parse("0 1 UDP 2113667327 203.0.113.1 54400 typ host")
        }
    }

    "leading/trailing spaces around a value are ignored" {
        with(LenientSdpContext) {
            SourceFilterAttribute.parse("  incl IN IP4 239.5.2.31 10.1.15.5  ") shouldBe
                SourceFilterAttribute.parse("incl IN IP4 239.5.2.31 10.1.15.5")
        }
    }

    "whole document with sprinkled extra spaces parses equal to the canonical one" {
        val canonical = """
            v=0
            o=jdoe 2890844526 2890842807 IN IP4 10.47.16.5
            s=SDP Seminar
            c=IN IP4 224.2.17.12/127
            t=2873397496 2873404696
            m=audio 49170 RTP/AVP 0
            a=rtcp:65179 IN IP4 123.45.67.89
            a=source-filter: incl IN IP4 239.5.2.31 10.1.15.5
        """.trimIndent().lines().joinToString("\r\n")

        val spaced = """
            v=0
            o=jdoe 2890844526  2890842807 IN IP4  10.47.16.5
            s=SDP Seminar
            c=IN  IP4 224.2.17.12/127
            t=2873397496  2873404696
            m=audio 49170 RTP/AVP 0
            a=rtcp:65179  IN IP4  123.45.67.89
            a=source-filter: incl IN IP4  239.5.2.31 10.1.15.5
        """.trimIndent().lines().joinToString("\r\n")

        // canonical is conforming, so the default (strict) parse equals the lenient parse of the spaced form.
        SdpSessionDescription.parse(spaced, strict = false) shouldBe SdpSessionDescription.parse(canonical)
    }
})
