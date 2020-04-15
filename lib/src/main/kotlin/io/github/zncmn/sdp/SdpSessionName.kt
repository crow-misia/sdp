package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpSessionName @JvmOverloads constructor(
    var name: String = " "
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("s=")
            append(name)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpSessionName {
            return SdpSessionName(line.substring(2))
        }
    }
}
