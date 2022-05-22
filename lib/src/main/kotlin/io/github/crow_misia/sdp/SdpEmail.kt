package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.6. Email Address.
 * e=<email-address>
 */
data class SdpEmail internal constructor(
    var address: String,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        append(address)
        appendSdpLineSeparator()
    }

    companion object {
        internal const val fieldPart = "e="

        @JvmStatic
        fun of(address: String): SdpEmail {
            return SdpEmail(address = address)
        }

        internal fun parse(line: String): SdpEmail {
            return SdpEmail(address = line.substring(2))
        }
    }
}
