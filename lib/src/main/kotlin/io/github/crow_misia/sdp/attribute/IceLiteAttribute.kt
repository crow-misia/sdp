package io.github.crow_misia.sdp.attribute

object IceLiteAttribute : SdpAttribute() {
    internal const val fieldName = "ice-lite"

    override val field = fieldName

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
