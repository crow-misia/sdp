package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class PtimeAttribute internal constructor(
    var time: Long,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(time)
    }

    companion object {
        internal const val fieldName = "ptime"

        @JvmStatic
        fun of(time: Long): PtimeAttribute {
            return PtimeAttribute(time)
        }

        internal fun parse(value: String): SdpAttribute {
            val time = value.toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $value as PtimeAttribute")
            }
            return PtimeAttribute(time)
        }
    }
}
