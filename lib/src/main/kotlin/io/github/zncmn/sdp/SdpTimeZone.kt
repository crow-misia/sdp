package io.github.zncmn.sdp

import java.lang.StringBuilder

class SdpTimeZone(
    var adjustmentTime: Long,
    var offset: Int
) {
    val value: StringBuilder
        get() = StringBuilder().apply {
            append(adjustmentTime)
            append(' ')
            append(offset)
        }
}
