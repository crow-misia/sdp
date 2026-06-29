package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.SourceFilterAttribute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

/**
 * Strict whitespace handling is the default ([StrictSdpContext]): positional
 * fields must be separated by exactly one `SP` per RFC 8866/4570. Non-conforming
 * runs of spaces are not silently repaired -- arity-checked fields reject them,
 * and others end up misaligned. Callers that need to tolerate extra whitespace
 * opt in with `strict = false` (see [AbnfLenientWhitespaceTest]).
 */
class AbnfStrictWhitespaceTest : StringSpec({
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

    "parse is strict by default and accepts conforming input" {
        // No strict argument -> strict mode; conforming single-SP input parses cleanly.
        SdpSessionDescription.parse(canonical) shouldBe SdpSessionDescription.parse(canonical, strict = true)
    }

    "strict (default) rejects a run of spaces between arity-checked fields" {
        // o= requires exactly 6 fields; the double space yields empty tokens -> reject.
        shouldThrow<SdpParseException> {
            SdpSessionDescription.parse(spaced)
        }
    }

    "strict origin parse throws on extra spaces" {
        shouldThrow<SdpParseException> {
            with(StrictSdpContext) { SdpOrigin.parse("o=jdoe 2890844526  2890842807 IN IP4 10.47.16.5") }
        }
    }

    "strict does not repair a non-arity-checked field -- it misaligns" {
        // source-filter only checks size >= 4, so a double space is not rejected but shifts
        // the fields right: destAddress becomes "" and the real address leaks into srcList.
        val misaligned = with(StrictSdpContext) { SourceFilterAttribute.parse("incl IN IP4  239.1.1.1 0.0.0.0") }
        misaligned shouldNotBe SourceFilterAttribute.of("incl", "IN", "IP4", "239.1.1.1", "0.0.0.0")
    }

    "strict and lenient agree on conforming input" {
        val strict = with(StrictSdpContext) { SourceFilterAttribute.parse("incl IN IP4 239.5.2.31 10.1.15.5") }
        val lenient = with(LenientSdpContext) { SourceFilterAttribute.parse("incl IN IP4 239.5.2.31 10.1.15.5") }
        strict shouldBe lenient
    }
})
