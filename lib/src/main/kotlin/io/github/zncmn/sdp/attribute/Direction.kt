package io.github.zncmn.sdp.attribute

import java.util.*

enum class Direction {
    SENDONLY,
    RECVONLY,
    SENDRECV,
    INACTIVE
    ;

    val value = getKey(name)

    companion object {
        private val MAPPING = values().associateBy { it.value }

        @JvmStatic
        fun of(str: String): Direction? {
            return MAPPING[getKey(str)]
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun getKey(name: String) = name.toLowerCase(Locale.ENGLISH)
