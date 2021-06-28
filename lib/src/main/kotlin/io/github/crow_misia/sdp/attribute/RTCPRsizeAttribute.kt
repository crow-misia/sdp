package io.github.crow_misia.sdp.attribute

object RTCPRsizeAttribute : SdpAttribute() {
    internal const val fieldName = "rtcp-rsize"

    override val field = fieldName

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
