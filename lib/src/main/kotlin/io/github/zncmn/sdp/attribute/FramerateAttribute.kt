package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class FramerateAttribute internal constructor(
    var value: Double
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
            append(value)
            append("\r\n")
        }
    }

    companion object {
        internal const val FIELD_NAME = "framerate"

        @JvmStatic
        fun of(value: Int): FramerateAttribute {
            return FramerateAttribute(value.toDouble())
        }

        @JvmStatic
        fun of(value: Double): FramerateAttribute {
            return FramerateAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            val rate = value.toDoubleOrNull() ?: run {
                throw SdpParseException("could not parse: $value as FramerateAttribute")
            }

            return FramerateAttribute(rate)
        }
    }
}