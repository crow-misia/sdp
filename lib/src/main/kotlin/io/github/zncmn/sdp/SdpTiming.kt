package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpTiming(
    var startTime: Long,
    var stopTime: Long,
    var repeatTime: SdpRepeatTime? = null
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("t=")
            append(startTime)
            append(' ')
            append(stopTime)
            append("\r\n")
            repeatTime?.joinTo(this)
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpTiming {
            val values = line.substring(2).split(' ')
            if (values.size != 2) {
                throw SdpParseException("could not parse: $line as Timing")
            }
            val startTime = values[0].toLongOrNull()
            val stopTime = values[1].toLongOrNull()
            if (startTime == null || stopTime == null) {
                throw SdpParseException("could not parse: $line as Timing")
            }
            return SdpTiming(startTime, stopTime)
        }
    }
}
