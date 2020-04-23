@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class FormatAttribute internal constructor(
    var format: Int
) : WithParametersAttribute() {
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

   override fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
            append(format)
            super.valueJoinTo(buffer)
        }
    }

    companion object {
        internal const val FIELD_NAME = "fmtp"

        @JvmStatic @JvmOverloads
        fun of(format: Int, parameters: String? = null): FormatAttribute {
            return FormatAttribute(format).also {
                it.setParameter(parameters)
            }
        }

        internal fun parse(value: String): FormatAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 1) {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            val format = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            return of(
                format = format,
                parameters = if (size > 1) values[1] else null
            )
        }
    }
}