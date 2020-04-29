package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class GroupAttribute internal constructor(
    var type: String,
    var mids: MutableList<String>
) : SdpAttribute {
    override val field = FIELD_NAME

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append(':')
            valueJoinTo(this)
            append("\r\n")
        }
    }

    private fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
            append(type)
            mids.forEach {
                append(' ')
                append(it)
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "group"

        @JvmStatic
        fun of(type: String, vararg mids: String): GroupAttribute {
            return GroupAttribute(type, mids.toMutableList())
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as GroupsAttribute")
            }
            return GroupAttribute(
                type = values[0],
                mids = values[1].split(' ').toMutableList()
            )
        }
    }
}