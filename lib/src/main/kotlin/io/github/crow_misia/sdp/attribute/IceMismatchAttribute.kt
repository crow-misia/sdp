package io.github.crow_misia.sdp.attribute

/**
 * RFC 5245 21.1.4. ice-mismatch.
 * Name: ice-mismatch
 * Value:
 * Usage Level: session
 * Charset Dependent: no
 * Syntax:
 * ice-mismatch           = "ice-mismatch"
 */
object IceMismatchAttribute : SdpAttribute() {
    internal const val FIELD_NAME = "ice-mismatch"

    override val field = FIELD_NAME

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
