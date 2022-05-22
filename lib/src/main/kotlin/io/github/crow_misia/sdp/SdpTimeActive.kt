package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.9. Time Active.
 * t=<start-time> <stop-time>
 */
data class SdpTimeActive internal constructor(
    var startTime: Long,
    var stopTime: Long,
    var repeatTime: SdpRepeatTimes?,
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
            repeatTime: SdpRepeatTimes? = null,
        ): SdpTimeActive {
            return SdpTimeActive(startTime, stopTime, repeatTime)
        }

        internal fun parse(line: String): SdpTimeActive {
            val values = line.substring(2).split(' ')
            if (values.size != 2) {
                throw SdpParseException("could not parse: $line as Timing")
            }
            val startTime = values[0].toLongOrNull()
            val stopTime = values[1].toLongOrNull()
            if (startTime == null || stopTime == null) {
                throw SdpParseException("could not parse: $line as Timing")
            }
            return SdpTimeActive(startTime, stopTime, null)
        }
    }
}
