package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class PtimeAttribute internal constructor(
    var time: Long
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
            append(time)
            append("\r\n")
        }
    }

    private fun valueJoinTo(buffer: StringBuilder) {
        buffer.append(time)
    }

    companion object {
        internal const val FIELD_NAME = "ptime"

        @JvmStatic
        fun of(streamId: Long): PtimeAttribute {
            return PtimeAttribute(streamId)
        }

        internal fun parse(value: String): PtimeAttribute {
            val time = value.toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $value as PtimeAttribute")
            }
            return PtimeAttribute(time)
        }
    }
}