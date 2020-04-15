package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpEmail(
    var address: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("e=")
            append(address)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpEmail {
            return SdpEmail(line.substring(2))
        }
    }
}
