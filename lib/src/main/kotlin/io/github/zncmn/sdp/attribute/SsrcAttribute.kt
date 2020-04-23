package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class SsrcAttribute internal constructor(
    var id: Long,
    var attribute: String,
    var ssrcValue: String
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
            append(id)
            append(' ')
            append(attribute)
            append(':')
            append(ssrcValue)
        }
    }

    companion object {
        internal const val FIELD_NAME = "ssrc"

        @JvmStatic
        fun of(id: Long, attribute: String, ssrcValue: String): SsrcAttribute {
            return SsrcAttribute(id, attribute, ssrcValue)
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as SsrcAttribute")
            }
            val id = values[0].toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $value as SsrcAttribute")
            }
            val ssrcValues = values[1].split(':', limit = 2)
            val ssrcSize = ssrcValues.size
            if (ssrcSize < 2) {
                throw SdpParseException("could not parse: $value as SsrcAttribute")
            }
            return SsrcAttribute(id, ssrcValues[0], ssrcValues[1])
        }
    }
}