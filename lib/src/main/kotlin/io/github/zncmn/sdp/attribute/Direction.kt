package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.Utils

enum class Direction {
    SENDONLY,
    RECVONLY,
    SENDRECV,
    INACTIVE
    ;

    val value = Utils.getName(name)

    companion object {
        private val MAPPING = values().associateBy { it.value }

        @JvmStatic
        fun of(str: String): Direction? {
            return MAPPING[Utils.getName(str)]
        }
    }
}
