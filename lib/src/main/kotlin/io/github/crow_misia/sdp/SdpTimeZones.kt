@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp

data class SdpTimeZones internal constructor(
    var timeZones: List<SdpTimeZone>
) : SdpElement {
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
        fun of(vararg timeZones: SdpTimeZone): SdpTimeZones {
            return SdpTimeZones(timeZones.toList())
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
            return SdpTimeZones(timeZones.toMutableList())
        }
    }
}
