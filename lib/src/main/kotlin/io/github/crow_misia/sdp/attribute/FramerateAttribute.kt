package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8866 6.13. framerate (Frame Rate)
 * Name: framerate
 * Value: framerate-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * framerate-value = non-zero-int-or-real
 * Example:
 * a=framerate:60
 */
data class FramerateAttribute internal constructor(
    var value: Double,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        val valueLong = value.toLong()
        if (value == valueLong.toDouble()) {
            append(valueLong)
        } else {
            append(value)
        }
    }

    companion object {
        internal const val fieldName = "framerate"

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
