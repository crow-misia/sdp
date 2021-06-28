package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SdpTiming internal constructor(
    var startTime: Long,
    var stopTime: Long,
    var repeatTime: SdpRepeatTime?,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        append(startTime)
        append(' ')
        append(stopTime)
        appendSdpLineSeparator()
        repeatTime?.joinTo(this)
    }

    companion object {
        internal const val fieldPart = "t="

        @JvmStatic
        @JvmOverloads
        fun of(
            startTime: Long = 0,
            stopTime: Long = 0,
            repeatTime: SdpRepeatTime? = null,
        ): SdpTiming {
            return SdpTiming(startTime, stopTime, repeatTime)
        }

        internal fun parse(line: String): SdpTiming {
            val values = line.substring(2).split(' ')
            if (values.size != 2) {
                throw SdpParseException("could not parse: $line as Timing")
            }
            val startTime = values[0].toLongOrNull()
            val stopTime = values[1].toLongOrNull()
            if (startTime == null || stopTime == null) {
                throw SdpParseException("could not parse: $line as Timing")
            }
            return SdpTiming(startTime, stopTime, null)
        }
    }
}
