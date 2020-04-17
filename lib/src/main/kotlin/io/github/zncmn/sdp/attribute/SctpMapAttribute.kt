package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class SctpMapAttribute internal constructor(
    var sctpmapNumber: Int,
    var app: String,
    var maxMessageSize: Int
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
            append(sctpmapNumber)
            append(' ')
            append(app)
            append(' ')
            append(maxMessageSize)
        }
    }

    companion object {
        internal const val FIELD_NAME = "sctpmap"

        @JvmStatic
        fun of(sctpmapNumber: Int, app: String, maxMessageSize: Int): SctpMapAttribute {
            return SctpMapAttribute(sctpmapNumber, app, maxMessageSize)
        }

        internal fun parse(value: String): SctpMapAttribute {
            val values = value.split(' ', limit = 3)
            val size = values.size
            if (size != 3) {
                throw SdpParseException("could not parse: $value as SctpMapAttribute")
            }
            val sctpmapNumber = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as SctpMapAttribute")
            }
            val maxMessageSize = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as SctpMapAttribute")
            }
            return SctpMapAttribute(sctpmapNumber, values[1], maxMessageSize)
        }
    }
}