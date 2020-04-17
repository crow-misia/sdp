@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp

data class SdpRepeatTime internal constructor(
    var repeatInterval: Int,
    var activeDuration: Int,
    internal var _offsets: MutableList<Int>
) : SdpElement {
    var offsets: List<Int>
        get() = _offsets
        set(value) { _offsets = ArrayList(value) }

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        val offsets = _offsets

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
        fun of(repeatInterval: Int,
               activeDuration: Int,
               offsets: List<Int>
        ): SdpRepeatTime {
            return SdpRepeatTime(repeatInterval, activeDuration, ArrayList(offsets))
        }

        internal fun parse(line: String): SdpRepeatTime {
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
            return of(values[0], values[1], values.subList(2, size))
        }
    }
}
