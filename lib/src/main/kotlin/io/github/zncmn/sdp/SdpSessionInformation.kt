package io.github.zncmn.sdp

import java.lang.StringBuilder

data class SdpSessionInformation(
    var description: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("i=")
            append(description)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpSessionInformation {
            return SdpSessionInformation(line.substring(2))
        }
    }
}
