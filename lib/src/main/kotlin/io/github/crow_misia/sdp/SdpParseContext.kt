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
     * [limit] behaves like [String.split]: a positive value caps the number of
     * returned parts and the final part keeps any remaining separator spaces, so
     * free-form remainders (fmtp params, crypto session-params, ...) survive
     * intact.
     */
    fun splitOnSpaces(input: String, limit: Int = 0): List<String>
}

/**
 * Spec-conforming policy: exactly one `SP` between fields. A non-conforming run
 * of spaces therefore yields empty tokens, which the per-field arity checks
 * reject -- so malformed whitespace surfaces as a parse failure rather than
 * silently misaligned fields. This is the default.
 */
internal object StrictSdpContext : SdpParseContext {
    override fun splitOnSpaces(input: String, limit: Int): List<String> =
        input.trim().split(' ', limit = limit)
}

/**
 * Tolerant policy: collapses runs of `SP` so that producers emitting extra
 * whitespace (e.g. `a=source-filter: incl IN IP4  239.1.1.1 0.0.0.0`) still
 * parse with their fields aligned. Implemented as a single forward scan, without
 * a regular expression.
 */
internal object LenientSdpContext : SdpParseContext {
    override fun splitOnSpaces(input: String, limit: Int): List<String> {
        val result = mutableListOf<String>()
        var start = 0
        var i = 0
        while (i < input.length) {
            // Once limit - 1 parts are produced, the rest is one final part.
            if (limit > 0 && result.size == limit - 1) {
                break
            }
            if (input[i] == ' ') {
                if (i > start) {
                    result.add(input.substring(start, i))
                }
                while (i < input.length && input[i] == ' ') {
                    i++
                }
                start = i
            } else {
                i++
            }
        }
        if (start < input.length) {
            result.add(input.substring(start))
        }
        return result
    }
}
