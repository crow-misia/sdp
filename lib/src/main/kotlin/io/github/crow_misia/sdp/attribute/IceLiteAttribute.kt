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
    internal const val fieldName = "ice-lite"

    override val field = fieldName

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
