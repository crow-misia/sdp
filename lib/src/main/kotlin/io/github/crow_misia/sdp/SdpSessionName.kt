package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.3. Session Name.
 * s=<session name>
 */
data class SdpSessionName internal constructor(
    var name: String,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(lineType)
        append(name)
        appendSdpLineSeparator()
    }

    companion object {
        internal const val lineType = "s="

        @JvmStatic
        @JvmOverloads
        fun of(name: String? = null): SdpSessionName {
            return SdpSessionName(name = name.orEmpty().ifEmpty { "-" })
        }

        internal fun parse(line: String): SdpSessionName {
            return SdpSessionName(name = line.substring(2))
        }
    }
}
