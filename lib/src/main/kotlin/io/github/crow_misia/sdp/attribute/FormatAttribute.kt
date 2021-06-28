@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class FormatAttribute internal constructor(
    var format: Int,
) : WithParametersAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(format)
        super.valueJoinTo(this)
    }

    companion object {
        internal const val fieldName = "fmtp"

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
