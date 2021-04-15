package io.github.crow_misia.sdp.attribute

object RTCPRsizeAttribute : SdpAttribute {
    internal const val FIELD_NAME = "rtcp-rsize"

    override val field = FIELD_NAME

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append("\r\n")
        }
    }

    @JvmStatic
    fun of() = this
}