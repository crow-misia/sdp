package io.github.crow_misia.sdp.attribute

/**
 * RFC 5761 RTP and RTCP multiplexed on one port.
 * Name: rtcp-mux
 * Value:
 * Usage Level: media
 * Charset Dependent: no
 */
object RTCPMuxAttribute : SdpAttribute() {
    internal const val FIELD_NAME = "rtcp-mux"

    override val field = FIELD_NAME

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
