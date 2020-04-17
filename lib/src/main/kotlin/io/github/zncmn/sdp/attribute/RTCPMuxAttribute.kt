package io.github.zncmn.sdp.attribute

object RTCPMuxAttribute : SdpAttribute {
    internal const val FIELD_NAME = "rtcp-mux"

    override val field = FIELD_NAME
    override val value: String? = null

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