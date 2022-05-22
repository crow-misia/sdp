package io.github.crow_misia.sdp.attribute

/**
 * RFC 5245 21.1.5. ice-pwd.
 * Name: ice-pwd
 * Value:
 * Usage Level: session or media
 * Charset Dependent: no
 * Syntax:
 * ice-pwd-att           = "ice-pwd" ":" password
 * password              = 22*256ice-char
 */
data class IcePwdAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "ice-pwd"

        @JvmStatic
        fun of(value: String): IcePwdAttribute {
            return IcePwdAttribute(value.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}
