package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SdpUri internal constructor(
    var uri: String
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append("u=")
        append(uri)
        appendSdpLineSeparator()
    }

    companion object {
        @JvmStatic
        fun of(uri: String): SdpUri {
            return SdpUri(uri)
        }

        internal fun parse(line: String): SdpUri {
            return SdpUri(line.substring(2))
        }
    }
}
