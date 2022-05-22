package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8841 5. SDP "sctp-port" Attribute.
 * Name: sctp-port
 * Value: Integer
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * sctp-port-value = 1*5(DIGIT) ; DIGIT defined in RFC 4566
 *  The SCTP port range is between 0 and 65535 (both included).
 *  Leading zeroes MUST NOT be used.
 * Example:
 * a=sctp-port:5000
 */
data class SctpPortAttribute internal constructor(
    var portNumber: Int,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(portNumber)
    }

    companion object {
        internal const val fieldName = "sctp-port"

        @JvmStatic
        fun of(portNumber: Int): SctpPortAttribute {
            return SctpPortAttribute(portNumber)
        }

        internal fun parse(value: String): SdpAttribute {
            val portNumber = value.toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as SctpPortAttribute")
            }
            return SctpPortAttribute(portNumber)
        }
    }
}
