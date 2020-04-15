package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpVersion @JvmOverloads constructor(
    var version: Int = 0
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("v=")
            append(version)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpVersion {
            return SdpVersion(line.substring(2).toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $line as Version")
            })
        }
    }
}
