package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException
import java.util.*
import kotlin.collections.LinkedHashSet

data class GroupAttribute internal constructor(
    var type: String,
    private var _mids: MutableSet<String>
) : SdpAttribute {
    var mids: Set<String>
        get() = _mids
        set(value) {
            _mids = LinkedHashSet(value.map { getKey(it) })
        }

    override val field = FIELD_NAME

    fun addMid(mid: String) {
        _mids.add(getKey(mid))
    }

    fun hasMid(mid: String): Boolean {
        return _mids.contains(getKey(mid))
    }

    fun removeMid(mid: String): Boolean {
        return _mids.remove(getKey(mid))
    }

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
            return GroupAttribute(type, LinkedHashSet(mids.map { getKey(it) }))
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as GroupsAttribute")
            }
            return of(
                type = values[0],
                mids = *values[1].split(' ').toTypedArray()
            )
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun getKey(name: String) = name.toLowerCase(Locale.ENGLISH)
    }
}