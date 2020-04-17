package io.github.zncmn.sdp.attribute

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
    }
}