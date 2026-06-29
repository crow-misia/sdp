package io.github.crow_misia.sdp

/**
 * Whitespace-handling policy for the positional, `SP`-delimited SDP fields.
 *
 * SDP/RFC 8866 and RFC 4570 ABNF require exactly one `SP` between fields, but
 * real-world producers sometimes emit several. The two policies differ only in
 * how a run of spaces is treated; everything else about parsing is identical.
 *
 * It is threaded through the parsers as a Kotlin context parameter (rather than
 * an explicit flag on every `parse` function) so that line parsers can simply
 * call [splitOnSpaces] and the active policy is supplied implicitly.
 */
internal interface SdpParseContext {
    /**
     * Splits [input] on the inter-field separator, after trimming leading and
     * trailing spaces.
     *
     * @param input The raw string to be split.
     * @param startIndex The zero-based index in [input] from which to start parsing.
     * Leading spaces immediately following this offset will be skipped.
     * @param limit behaves like [String.split]: a positive value caps the number of
     * returned parts and the final part keeps any remaining separator spaces, so
     * free-form remainders (fmtp params, crypto session-params, ...) survive
     * intact.
     */
    fun splitOnSpaces(input: String, startIndex: Int, limit: Int = 0): List<String>
}

/**
 * Spec-conforming policy: exactly one `SP` between fields. A non-conforming run
 * of spaces therefore yields empty tokens, which the per-field arity checks
 * reject -- so malformed whitespace surfaces as a parse failure rather than
 * silently misaligned fields. This is the default.
 */
internal object StrictSdpContext : SdpParseContext {
    override fun splitOnSpaces(input: String, startIndex: Int, limit: Int): List<String> {
        val length = input.length
        val result = mutableListOf<String>()
        var start = startIndex
        while (start <= length) {
            // Once limit - 1 parts are produced, the rest is one final part.
            val spPos = if (limit > 0 && result.size == limit - 1) {
                -1
            } else {
                input.indexOf(' ', start)
            }
            if (spPos < 0) {
                break
            }
            result.add(input.substring(start, spPos))
            start = spPos + 1
        }
        if (start < length) {
            result.add(input.substring(start))
        }
        return result
    }
}

/**
 * Tolerant policy: collapses runs of `SP` so that producers emitting extra
 * whitespace (e.g. `a=source-filter: incl IN IP4  239.1.1.1 0.0.0.0`) still
 * parse with their fields aligned. Implemented as a single forward scan, without
 * a regular expression.
 */
internal object LenientSdpContext : SdpParseContext {
    override fun splitOnSpaces(input: String, startIndex: Int, limit: Int): List<String> {
        val length = input.length
        val result = mutableListOf<String>()
        var start = startIndex
        while (start < length) {
            // Once limit - 1 parts are produced, the rest is one final part.
            val spPos = if (limit > 0 && result.size == limit - 1) {
                -1
            } else {
                input.indexOf(' ', start)
            }
            if (spPos < 0) {
                break
            }
            if (spPos > start) {
                result.add(input.substring(start, spPos))
                start = spPos + 1
            }
            while (start < length && input[start] == ' ') {
                start++
            }
        }
        if (start < length) {
            result.add(input.substring(start))
        }
        return result
    }
}

internal class SdpAttributeSdpContext(
    private val delegated: SdpParseContext
) : SdpParseContext {
    override fun splitOnSpaces(input: String, startIndex: Int, limit: Int): List<String> {
        return if (input.startsWith(prefix = " ", startIndex = startIndex)) {
            delegated.splitOnSpaces(input = input, limit = limit, startIndex = startIndex + 1)
        } else {
            delegated.splitOnSpaces(input = input, limit = limit, startIndex = startIndex)
        }
    }
}
