@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8866 6.15. fmtp (Format Parameters)
 * Name: fmtp
 * Value: fmtp-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * fmtp-value = fmt SP format-specific-params
 * format-specific-params = byte-string
 * ; Notes:
 * ; - The format parameters are media type parameters and
 * ;   need to reflect their syntax.
 * Example:
 * a=fmtp:96 profile-level-id=42e016;max-mbps=108000;max-fs=3600
 */
data class FmtpAttribute internal constructor(
    var format: Int,
) : WithParametersAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(format)
        super.valueJoinTo(this)
    }

    companion object {
        internal const val fieldName = "fmtp"

        @JvmStatic @JvmOverloads
        fun of(format: Int, parameters: String? = null): FmtpAttribute {
            return FmtpAttribute(format).also {
                it.setParameter(parameters)
            }
        }

        internal fun parse(value: String): FmtpAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 1) {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            val format = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            return of(
                format = format,
                parameters = if (size > 1) values[1] else null
            )
        }
    }
}
