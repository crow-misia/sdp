package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpConnection(
    var nettype: String,
    var addrtype: String,
    var connectionAddress: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("c=")
            append(nettype)
            append(' ')
            append(addrtype)
            append(' ')
            append(connectionAddress)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpConnection {
            val values = line.substring(2).split(' ')
            if (values.size != 3) {
                throw SdpParseException("could not parse: $line as Connection")
            }
            return SdpConnection(values[0], values[1], values[2])
        }
    }
}
