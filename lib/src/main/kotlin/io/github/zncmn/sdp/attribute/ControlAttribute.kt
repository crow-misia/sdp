package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class ControlAttribute internal constructor(
    var streamId: Int
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
        buffer.append(streamId)
    }

    companion object {
        internal const val FIELD_NAME = "control"

        @JvmStatic
        fun of(streamId: Int = 0): ControlAttribute {
            return ControlAttribute(streamId)
        }

        internal fun parse(value: String): ControlAttribute {
            val streamId = value.toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as ControlAttribute")
            }
            return ControlAttribute(streamId)
        }
    }
}