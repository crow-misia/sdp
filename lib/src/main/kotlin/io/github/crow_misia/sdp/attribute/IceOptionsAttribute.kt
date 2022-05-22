package io.github.crow_misia.sdp.attribute

/**
 * RFC 5245 21.1.7. ice-options.
 * Name: ice-options
 * Value:
 * Usage Level: session
 * Charset Dependent: no
 * Syntax:
 * ice-options           = "ice-options" ":" ice-option-tag 0*(SP ice-option-tag)
 * ice-option-tag        = 1*ice-char
 */
data class IceOptionsAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "ice-options"

        @JvmStatic
        fun of(value: String): IceOptionsAttribute {
            return IceOptionsAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return IceOptionsAttribute(value)
        }
    }
}
