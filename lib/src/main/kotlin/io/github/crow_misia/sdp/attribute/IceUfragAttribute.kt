package io.github.crow_misia.sdp.attribute

/**
 * RFC 5245 21.1.6. ice-ufrag.
 * Name: ice-ufrag
 * Value:
 * Usage Level: session or media
 * Charset Dependent: no
 * Syntax:
 * ice-ufrag-att         = "ice-ufrag" ":" ufrag
 * ufrag                 = 4*256ice-char
 */
data class IceUfragAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "ice-ufrag"

        @JvmStatic
        fun of(value: String): IceUfragAttribute {
            return IceUfragAttribute(value.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}
