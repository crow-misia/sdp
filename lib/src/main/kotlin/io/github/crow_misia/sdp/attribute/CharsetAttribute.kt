package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8866 6.10. charset (Character Set)
 * Name: charset
 * Value: charset-value
 * Usage Level: session
 * Charset Dependent: no
 * Syntax:
 * charset-value = <defined in \[RFC2978\]>
 */
data class CharsetAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    companion object {
        internal const val fieldName = "charset"

        @JvmStatic
        fun of(value: String): CharsetAttribute {
            return CharsetAttribute(value)
        }

        internal fun parse(value: String): CharsetAttribute {
            return CharsetAttribute(value)
        }
    }
}
