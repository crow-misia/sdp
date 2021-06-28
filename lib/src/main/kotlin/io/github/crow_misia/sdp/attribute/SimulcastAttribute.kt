package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class SimulcastAttribute internal constructor(
    var dir1: StreamDirection,
    var list1: String,
    var dir2: StreamDirection,
    var list2: String,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(dir1.value)
        append(' ')
        append(list1)
        if (dir2 != StreamDirection.NONE && list2.isNotEmpty()) {
            append(' ')
            append(dir2.value)
            append(' ')
            append(list2)
        }
    }

    companion object {
        internal const val fieldName = "simulcast"

        @JvmStatic
        @JvmOverloads
        fun of(
            dir1: StreamDirection,
            list1: String,
            dir2: StreamDirection = StreamDirection.NONE,
            list2: String = "",
        ): SimulcastAttribute {
            return SimulcastAttribute(dir1, list1, dir2, list2)
        }

        internal fun parse(value: String): SdpAttribute {
            if (value.startsWith(' ')) {
                return Simulcast03Attribute.of(value.substring(1))
            }
            val values = value.split(' ', limit = 4)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as SimulcastAttribute")
            }
            return of(
                dir1 = StreamDirection.of(values[0]),
                list1 = values[1],
                dir2 = if (size > 2) StreamDirection.of(values[2]) else StreamDirection.NONE,
                list2 = if (size > 3) values[3] else ""
            )
        }
    }
}
