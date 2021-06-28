package io.github.crow_misia.sdp.attribute

object RTCPMuxAttribute : SdpAttribute() {
    internal const val fieldName = "rtcp-mux"

    override val field = fieldName

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
