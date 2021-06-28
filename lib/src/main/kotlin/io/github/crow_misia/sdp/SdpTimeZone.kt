package io.github.crow_misia.sdp

data class SdpTimeZone internal constructor(
    val adjustmentTime: Long,
    val offset: String,
): SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
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
