package io.github.crow_misia.sdp.attribute

/**
 * RFC 5761 RTP and RTCP multiplexed on one port.
 * Name: rtcp-mux
 * Value:
 * Usage Level: media
 * Charset Dependent: no
 */
object RTCPMuxAttribute : SdpAttribute() {
    internal const val fieldName = "rtcp-mux"

    override val field = fieldName

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
