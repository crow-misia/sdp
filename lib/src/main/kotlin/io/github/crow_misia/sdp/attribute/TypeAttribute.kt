package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8866 6.9. type (Conference Type)
 * Name: type
 * Value: type-value
 * Usage Level: session
 * Charset Dependent: no
 * Syntax:
 * type-value = conference-type
 * conference-type = broadcast / meeting / moderated / test / H332
 * broadcast = %s"broadcast"
 * meeting   = %s"meeting"
 * moderated = %s"moderated"
 * test      = %s"test"
 * H332      = %s"H332"
 * ; NOTE: These names are case-sensitive.
 * Example:
 * a=type:moderated
 */
data class TypeAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    companion object {
        internal const val fieldName = "type"

        @JvmStatic
        fun of(value: String): TypeAttribute {
            return TypeAttribute(value)
        }

        internal fun parse(value: String): TypeAttribute {
            return TypeAttribute(value)
        }
    }
}
