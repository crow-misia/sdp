package io.github.zncmn.sdp.attribute

import java.util.*

enum class Direction {
    SENDONLY,
    RECVONLY,
    SENDRECV,
    INACTIVE
    ;

    companion object {
        private val MAPPING = values().associateBy { it.name.toLowerCase() }

        @JvmStatic
        fun of(str: String): Direction? {
            return MAPPING[str.toLowerCase()]
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun getKey(name: String) = name.toLowerCase(Locale.ENGLISH)
    }
}