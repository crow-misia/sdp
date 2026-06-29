@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator
import io.github.crow_misia.sdp.Utils.splitOnSpaces

/**
 * RFC 8866 5.10. Repeat Times.
 * r=<repeat interval> <active duration> <offsets from start-time>
 */
data class SdpRepeatTimes internal constructor(
    var repeatInterval: String,
    var activeDuration: String,
    var offsets: MutableList<String>,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(FIELD_PART)
        append(repeatInterval)
        append(' ')
        append(activeDuration)
        offsets.forEach {
            append(' ')
            append(it)
        }
        appendSdpLineSeparator()
    }

    companion object {
        internal const val FIELD_PART = "r="

        @JvmStatic
        fun of(
            repeatInterval: String,
            activeDuration: String,
            offsets: List<String>,
        ): SdpRepeatTimes {
            return SdpRepeatTimes(repeatInterval, activeDuration, offsets.toMutableList())
        }

        @JvmStatic
        fun of(
            repeatInterval: String,
            activeDuration: String,
            vararg offsets: String,
        ): SdpRepeatTimes {
            return SdpRepeatTimes(repeatInterval, activeDuration, offsets.toMutableList())
        }

        context(_: SdpParseContext)
        internal fun parse(line: String): SdpRepeatTimes {
            val values = line.splitOnSpaces(startIndex = 2)
            val size = values.size
            if (size < 3) {
                throw SdpParseException("could not parse: $line as RepeatTime")
            }
            return SdpRepeatTimes(values[0], values[1], values.subList(2, size).toMutableList())
        }
    }
}
