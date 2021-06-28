@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class RidAttribute internal constructor(
    var id: String,
    var direction: StreamDirection,
) : WithParametersAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(id)
        append(' ')
        append(direction.value)
        super.valueJoinTo(this)
    }

    companion object {
        internal const val fieldName = "rid"

        @JvmStatic
        @JvmOverloads
        fun of(id: String, direction: StreamDirection, parameters: String? = null): RidAttribute {
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
                direction = StreamDirection.of(values[1]),
                parameters = if (size > 2) values[2] else null
            )
        }
    }
}
