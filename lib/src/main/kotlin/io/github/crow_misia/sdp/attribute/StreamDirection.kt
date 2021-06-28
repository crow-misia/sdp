package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.Utils

enum class StreamDirection(
    val value: String,
) {
    SEND("send"),
    RECV("recv"),
    NONE("")
    ;

    companion object {
        private val MAPPING = values().associateBy { it.value }

        @JvmStatic
        fun of(str: String?): StreamDirection {
            return MAPPING[Utils.getName(str)] ?: NONE
        }
    }
}
