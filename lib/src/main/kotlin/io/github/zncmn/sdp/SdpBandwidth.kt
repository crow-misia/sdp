package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpBandwidth(
    var bwtype: String,
    var bandwidth: String
) : SdpElement {
    constructor(type: String, bandwidth: Int) : this(type, bandwidth.toString())

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("b=")
            append(bwtype)
            append(':')
            append(bandwidth)
            append("\r\n")
        }
    }


    companion object {
        @JvmStatic
        fun parse(line: String): SdpBandwidth {
            val values = line.substring(2).split(':')
            if (values.size != 2) {
                throw SdpParseException("could not parse: $line as Bandwidth")
            }
            return SdpBandwidth(values[0], values[1])
        }
    }
}
