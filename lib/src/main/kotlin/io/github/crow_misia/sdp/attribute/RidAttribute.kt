@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class RidAttribute internal constructor(
    var id: String,
    var direction: String
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
            append(id)
            append(' ')
            append(direction)
            super.valueJoinTo(buffer)
        }
    }

    companion object {
        internal const val FIELD_NAME = "rid"

        @JvmStatic @JvmOverloads
        fun of(id: String, direction: String, parameters: String? = null): RidAttribute {
            return RidAttribute(id, direction).also {
                it.setParameter(parameters)
            }
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 3)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as RidAttribute")
            }
            return of(
                id = values[0],
                direction = values[1],
                parameters = if (size > 2) values[2] else null
            )
        }
    }
}