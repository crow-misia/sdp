package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class RTCPAttribute internal constructor(
    var port: Int,
    var nettype: String,
    var addrtype: String,
    var address: String
) : SdpAttribute {
    override val field = FIELD_NAME

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
            if (address.isNotEmpty()) {
                append(' ')
                append(nettype)
                append(' ')
                append(addrtype)
                append(' ')
                append(address)
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "rtcp"

        @JvmStatic
        fun of(port: Int): RTCPAttribute {
            return RTCPAttribute(port, "", "", "")
        }

        @JvmStatic
        fun of(port: Int, nettype: String, addrtype: String, address: String): RTCPAttribute {
            return RTCPAttribute(port, nettype, addrtype, address)
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 4)
            val port = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as RtcpAttribute")
            }
            return if (values.size == 4) {
                RTCPAttribute(port, values[1], values[2], values[3])
            } else {
                RTCPAttribute(port, "", "", "")
            }
        }
    }
}