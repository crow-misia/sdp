package io.github.crow_misia.sdp.attribute

object ExtmapAllowMixedAttribute : SdpAttribute() {
    internal const val FIELD_NAME = "extmap-allow-mixed"

    override val field = FIELD_NAME

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
