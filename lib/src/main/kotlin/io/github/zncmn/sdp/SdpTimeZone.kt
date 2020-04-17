package io.github.zncmn.sdp

data class SdpTimeZone internal constructor(
    var adjustmentTime: Long,
    var offset: Int
) {
    val value: StringBuilder
        get() = StringBuilder().apply {
            append(adjustmentTime)
            append(' ')
            append(offset)
        }

    companion object {
        @JvmStatic
        fun of(adjustmentTime: Long, offset: Int): SdpTimeZone {
            return SdpTimeZone(adjustmentTime, offset)
        }
    }
}
