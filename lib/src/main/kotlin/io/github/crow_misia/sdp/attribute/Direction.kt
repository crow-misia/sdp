package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.Utils

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
