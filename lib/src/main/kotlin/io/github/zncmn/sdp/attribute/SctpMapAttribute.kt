package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class SctpMapAttribute internal constructor(
    var sctpmapNumber: Int,
    var app: String,
    var maxMessageSize: Int?
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
            append(sctpmapNumber)
            append(' ')
            append(app)
            maxMessageSize?.also {
                append(' ')
                append(it)
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "sctpmap"

        @JvmStatic @JvmOverloads
        fun of(sctpmapNumber: Int, app: String, maxMessageSize: Int? = null): SctpMapAttribute {
            return SctpMapAttribute(sctpmapNumber, app, maxMessageSize)
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 3)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as SctpMapAttribute")
            }
            val sctpmapNumber = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as SctpMapAttribute")
            }
            val maxMessageSize = if (size > 2) values[2].toIntOrNull() else null
            return SctpMapAttribute(sctpmapNumber, values[1], maxMessageSize)
        }
    }
}