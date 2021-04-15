package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class MaxPtimeAttribute internal constructor(
    var time: Long
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
            append(time)
            append("\r\n")
        }
    }

    private fun valueJoinTo(buffer: StringBuilder) {
        buffer.append(time)
    }

    companion object {
        internal const val FIELD_NAME = "maxptime"

        @JvmStatic
        fun of(streamId: Long): MaxPtimeAttribute {
            return MaxPtimeAttribute(streamId)
        }

        internal fun parse(value: String): SdpAttribute {
            val time = value.toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $value as MaxPtimeAttribute")
            }
            return MaxPtimeAttribute(time)
        }
    }
}