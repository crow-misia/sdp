package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpUri(
    var uri: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("u=")
            append(uri)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpUri {
            return SdpUri(line.substring(2))
        }
    }
}
