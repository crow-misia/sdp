package io.github.crow_misia.sdp.attribute

object ExtmapAllowMixedAttribute : SdpAttribute() {
    internal const val fieldName = "extmap-allow-mixed"

    override val field = fieldName

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
