package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseContext
import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils.splitOnSpaces

data class GroupAttribute internal constructor(
    var type: String,
    var mids: MutableList<String>,
) : SdpAttribute() {
    override val field = FIELD_NAME

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(type)
        mids.forEach {
            append(' ')
            append(it)
        }
    }

    companion object {
        internal const val FIELD_NAME = "group"

        @JvmStatic
        fun of(type: String, mids: List<String>): GroupAttribute {
            return GroupAttribute(type, mids.toMutableList())
        }

        @JvmStatic
        fun of(type: String, vararg mids: String): GroupAttribute {
            return GroupAttribute(type, mids.toMutableList())
        }

        context(_: SdpParseContext)
        internal fun parse(value: String): SdpAttribute {
            val values = value.splitOnSpaces(limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as GroupsAttribute")
            }
            return GroupAttribute(
                type = values[0],
                mids = values[1].splitOnSpaces().toMutableList()
            )
        }
    }
}
