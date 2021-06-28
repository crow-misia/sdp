package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils
import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SimulcastAttribute internal constructor(
    var dir1: String,
    var list1: String,
    var dir2: String?,
    var list2: String?
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(dir1)
        append(' ')
        append(list1)
        if (!dir2.isNullOrBlank()) {
            append(' ')
            append(dir2)
            append(' ')
            append(list2)
        }
    }

    companion object {
        internal const val fieldName = "simulcast"

        @JvmStatic
        fun of(dir1: String, list1: String, dir2: String? = null, list2: String? = null): SimulcastAttribute {
            return SimulcastAttribute(Utils.getName(dir1), list1, dir2?.let { Utils.getName(it) }, list2)
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
                dir1 = values[0],
                list1 = values[1],
                dir2 = if (size > 2) values[2] else null,
                list2 = if (size > 3) values[3] else null
            )
        }
    }
}
