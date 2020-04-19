package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class RTCPFbAttribute internal constructor(
    var payloadType: String,
    var type: String,
    var subtype: String?
) : SdpAttribute {
    override val field = FIELD_NAME
    override val value: String
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
            append(payloadType)
            append(' ')
            append(type)
            subtype?.also {
                append(' ')
                append(it)
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "rtcp-fb"

        @JvmStatic
        fun of(payloadType: String, type: String, subtype: String?): RTCPFbAttribute {
            return RTCPFbAttribute(payloadType, type, subtype)
        }

        internal fun parse(value: String): RTCPFbAttribute {
            val values = value.split(' ', limit = 3)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as RtcpFbAttribute")
            }
            return RTCPFbAttribute(
                payloadType = values[0],
                type = values[1],
                subtype = if (size < 3) null else values[2]
            )
        }
    }
}