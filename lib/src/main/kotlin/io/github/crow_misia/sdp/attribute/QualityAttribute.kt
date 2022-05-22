package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8866 6.14. quality
 * Name: quality
 * Value: quality-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * quality-value = zero-based-integer
 * Example:
 * a=quality:10
 */
data class QualityAttribute internal constructor(
    var value: Int,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(value)
    }

    companion object {
        internal const val fieldName = "quality"

        @JvmStatic
        fun of(value: Int): QualityAttribute {
            return QualityAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            val rate = value.toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as FramerateAttribute")
            }

            return QualityAttribute(rate)
        }
    }
}
