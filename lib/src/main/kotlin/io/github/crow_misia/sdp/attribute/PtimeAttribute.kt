package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils.toCompactString

/**
 * RFC8866 6.4. ptime (Packet Time)
 * Name: ptime
 * Value: ptime-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * ptime-value = non-zero-int-or-real
 * Example:
 * a=ptime:20
 */
data class PtimeAttribute internal constructor(
    var time: Double,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(time.toCompactString())
    }

    companion object {
        internal const val fieldName = "ptime"

        @JvmStatic
        fun of(time: Double): PtimeAttribute {
            return PtimeAttribute(time)
        }

        internal fun parse(value: String): SdpAttribute {
            val time = value.toDoubleOrNull() ?: run {
                throw SdpParseException("could not parse: $value as PtimeAttribute")
            }
            return PtimeAttribute(time)
        }
    }
}
