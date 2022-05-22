package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8866 6.11. sdplang (SDP Language)
 * Name: sdplang
 * Value: sdplang-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * sdplang-value = Language-Tag ; Language-Tag defined in RFC 5646
 * Example:
 * a=sdplang:fr
 */
data class SdplangAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    companion object {
        internal const val fieldName = "sdplang"

        @JvmStatic
        fun of(value: String): SdplangAttribute {
            return SdplangAttribute(value)
        }

        internal fun parse(value: String): SdplangAttribute {
            return SdplangAttribute(value)
        }
    }
}
