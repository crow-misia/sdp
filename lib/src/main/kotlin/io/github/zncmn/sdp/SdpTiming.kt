package io.github.zncmn.sdp

data class SdpTiming internal constructor(
    var startTime: Long,
    var stopTime: Long,
    var repeatTime: SdpRepeatTime?
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
        @JvmStatic @JvmOverloads
        fun of(startTime: Long,
               stopTime: Long,
               repeatTime: SdpRepeatTime? = null
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
            return of(startTime, stopTime, null)
        }
    }
}
