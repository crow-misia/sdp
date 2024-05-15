package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils.toCompactString

/**
 * RFC8866 6.5. maxptime (Maximum Packet Time)
 * Name: maxptime
 * Value: maxptime-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * maxptime-value = non-zero-int-or-real
 * Example:
 * a=maxptime:20
 */
data class MaxPtimeAttribute internal constructor(
    var time: Double,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(time.toCompactString())
    }

    companion object {
        internal const val fieldName = "maxptime"

        @JvmStatic
        fun of(time: Double): MaxPtimeAttribute {
            return MaxPtimeAttribute(time)
        }

        internal fun parse(value: String): SdpAttribute {
            val time = value.toDoubleOrNull() ?: run {
                throw SdpParseException("could not parse: $value as MaxPtimeAttribute")
            }
            return MaxPtimeAttribute(time)
        }
    }
}
