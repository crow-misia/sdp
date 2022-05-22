@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

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
        append(fieldPart)
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
        internal const val fieldPart = "r="

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

        internal fun parse(line: String): SdpRepeatTimes {
            val values = line.substring(2).split(' ')
            val size = values.size
            if (size < 3) {
                throw SdpParseException("could not parse: $line as RepeatTime")
            }
            return SdpRepeatTimes(values[0], values[1], values.subList(2, size).toMutableList())
        }
    }
}
