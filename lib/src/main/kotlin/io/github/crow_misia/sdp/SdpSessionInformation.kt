package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SdpSessionInformation internal constructor(
    var description: String
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append("i=")
        append(description)
        appendSdpLineSeparator()
    }

    companion object {
        @JvmStatic
        fun of(description: String): SdpSessionInformation {
            return SdpSessionInformation(description)
        }

        internal fun parse(line: String): SdpSessionInformation {
            return SdpSessionInformation(line.substring(2))
        }
    }
}
