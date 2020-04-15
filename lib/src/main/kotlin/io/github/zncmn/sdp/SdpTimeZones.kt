package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpTimeZones(
   timeZones: List<SdpTimeZone>
) : SdpElement {
    val timeZones = timeZones.toMutableList()

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("z=")
            timeZones.joinTo(this, " ") { it.value }
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpTimeZones {
            val values = line.substring(2).split(' ')
            val size = values.size
            if (size % 2 != 0) {
                throw SdpParseException("could not parse: $line as TimeZones")
            }
            val timeZones = (0 until size step 2).map { index ->
                val time = values[index].toLongOrNull()
                val offset = values[index + 1].toIntOrNull()
                if (time == null || offset == null) {
                    throw SdpParseException("could not parse: $line as TimeZones")
                }
                SdpTimeZone(time, offset)
            }
            return SdpTimeZones(timeZones)
        }
    }
}
