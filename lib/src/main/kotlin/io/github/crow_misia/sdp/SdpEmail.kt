package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SdpEmail internal constructor(
    var address: String
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append("e=")
        append(address)
        appendSdpLineSeparator()
    }

    companion object {
        @JvmStatic
        fun of(address: String): SdpEmail {
            return SdpEmail(address)
        }

        internal fun parse(line: String): SdpEmail {
            return SdpEmail(line.substring(2))
        }
    }
}
