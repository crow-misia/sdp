package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class RTCPFbAttribute internal constructor(
    var payloadType: String,
    var type: String,
    var subtype: String?,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(payloadType)
        append(' ')
        append(type)
        subtype?.also {
            append(' ')
            append(it)
        }
    }

    companion object {
        internal const val fieldName = "rtcp-fb"

        @JvmStatic
        fun of(payloadType: String, type: String, subtype: String?): RTCPFbAttribute {
            return RTCPFbAttribute(payloadType, type, subtype)
        }

        internal fun parse(value: String): SdpAttribute {
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
