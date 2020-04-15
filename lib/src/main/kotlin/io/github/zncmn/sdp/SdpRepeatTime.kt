package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpRepeatTime(
    var repeatInterval: Int,
    var activeDuration: Int,
    offsets: List<Int>
) : SdpElement {
    val offsets = offsets.toMutableList()

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        val offsets = offsets

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
        fun parse(line: String): SdpRepeatTime {
            val values = line.substring(2).split(' ')
                .map {
                    it.toIntOrNull() ?: run {
                        throw SdpParseException("could not parse: $line as RepeatTime")
                    }
                }
            val size = values.size
            if (size < 3) {
                throw SdpParseException("could not parse: $line as RepeatTime")
            }
            return SdpRepeatTime(values[0], values[1], values.subList(2, size))

        }
    }
}
