package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class SsrcAttribute internal constructor(
    var id: Long,
    var attribute: String,
    var ssrcValue: String,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(id)
        append(' ')
        append(attribute)
        if (ssrcValue.isNotBlank()) {
            append(':')
            append(ssrcValue)
        }
    }

    companion object {
        internal const val fieldName = "ssrc"

        @JvmStatic @JvmOverloads
        fun of(id: Long, attribute: String, ssrcValue: String? = null): SsrcAttribute {
            return SsrcAttribute(id, attribute, ssrcValue.orEmpty())
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
            return SsrcAttribute(id, ssrcValues[0], if (ssrcValues.size > 1) ssrcValues[1] else "")
        }
    }
}
