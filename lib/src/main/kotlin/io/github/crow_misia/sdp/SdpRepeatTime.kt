@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp

data class SdpRepeatTime internal constructor(
    var repeatInterval: String,
    var activeDuration: String,
    var offsets: List<String>
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("r=")
            append(repeatInterval)
            append(' ')
            append(activeDuration)
            offsets.forEach {
                append(' ')
                append(it)
            }
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun of(repeatInterval: String,
               activeDuration: String,
               vararg offsets: String
        ): SdpRepeatTime {
            return SdpRepeatTime(repeatInterval, activeDuration, offsets.toList())
        }

        internal fun parse(line: String): SdpRepeatTime {
            val values = line.substring(2).split(' ')
            val size = values.size
            if (size < 3) {
                throw SdpParseException("could not parse: $line as RepeatTime")
            }
            return SdpRepeatTime(values[0], values[1], values.subList(2, size))
        }
    }
}
