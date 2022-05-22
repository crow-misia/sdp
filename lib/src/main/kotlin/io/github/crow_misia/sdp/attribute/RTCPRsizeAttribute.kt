package io.github.crow_misia.sdp.attribute

/**
 * RFC 5506 Reduced-Size RTCP.
 * Name: rtpmap
 * Value:
 * Usage Level: media
 * Charset Dependent: no
 */
object RTCPRsizeAttribute : SdpAttribute() {
    internal const val fieldName = "rtcp-rsize"

    override val field = fieldName

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
