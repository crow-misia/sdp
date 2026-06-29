package io.github.crow_misia.sdp.attribute

/**
 * RFC 5245 21.1.3. ice-lite.
 * Name: ice-lite
 * Value:
 * Usage Level: session
 * Charset Dependent: no
 * Syntax:
 * ice-lite               = "ice-lite"
 */
object IceLiteAttribute : SdpAttribute() {
    internal const val FIELD_NAME = "ice-lite"

    override val field = FIELD_NAME

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
