@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SdpTimeZones internal constructor(
    var timeZones: MutableList<SdpTimeZone>,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        timeZones.forEachIndexed { index, timeZone ->
            if (index > 0) append(' ')
            timeZone.joinTo(this)
        }
        appendSdpLineSeparator()
    }

    companion object {
        internal const val fieldPart = "z="

        @JvmStatic
        fun of(timeZones: List<SdpTimeZone>): SdpTimeZones {
            return SdpTimeZones(timeZones.toMutableList())
        }

        @JvmStatic
        fun of(vararg timeZones: SdpTimeZone): SdpTimeZones {
            return SdpTimeZones(timeZones.toMutableList())
        }

        internal fun parse(line: String): SdpTimeZones {
            val values = line.substring(2).split(' ')
            val size = values.size
            if (size % 2 != 0) {
                throw SdpParseException("could not parse: $line as TimeZones")
            }
            val timeZones = (0 until size step 2).map { index ->
                val time = values[index].toLongOrNull() ?: run {
                    throw SdpParseException("could not parse: $line as TimeZones")
                }
                val offset = values[index + 1]
                SdpTimeZone.of(time, offset)
            }
            return of(timeZones)
        }
    }
}
