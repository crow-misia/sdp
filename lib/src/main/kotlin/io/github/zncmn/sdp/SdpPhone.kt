package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpPhone(
    var number: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("p=")
            append(number)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpPhone {
            return SdpPhone(line.substring(2))
        }
    }
}
