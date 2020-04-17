package io.github.zncmn.sdp

data class RTCPAttribute internal constructor(
    var port: Int,
    var nettype: String,
    var addrtype: String,
    var address: String
) : SdpAttribute {
    override val field = "rtcp"
    override val value: String?
        get() = buildString { valueJoinTo(this) }

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append(':')
            valueJoinTo(this)
            append("\r\n")
        }
    }

   private fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
            append(port)
            append(' ')
            append(nettype)
            append(' ')
            append(addrtype)
            append(' ')
            append(address)
        }
    }

    companion object {
        @JvmStatic
        fun of(port: Int, nettype: String, addrtype: String, address: String): RTCPAttribute {
            return RTCPAttribute(port, nettype, addrtype, address)
        }

        internal fun parse(value: String?): RTCPAttribute {
            value ?: run {
                throw SdpParseException("could not parse: $value as RtcpAttribute")
            }
            val values = value.split(' ')
            val size = values.size
            if (size != 4) {
                throw SdpParseException("could not parse: $value as RtcpAttribute")
            }
            val port = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as RtcpAttribute")
            }
            return of(port, values[1], values[2], values[3])
        }
    }
}