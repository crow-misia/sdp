package io.github.crow_misia.sdp

data class SdpTimeZone internal constructor(
    var adjustmentTime: Long,
    var offset: String
) {
    val value: StringBuilder
        get() = StringBuilder().apply {
            append(adjustmentTime)
            append(' ')
            append(offset)
        }

    companion object {
        @JvmStatic
        fun of(adjustmentTime: Long, offset: String): SdpTimeZone {
            return SdpTimeZone(adjustmentTime, offset)
        }
    }
}
