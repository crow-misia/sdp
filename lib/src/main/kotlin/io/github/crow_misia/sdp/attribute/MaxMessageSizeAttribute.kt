package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8841 6. SDP "max-message-size" Attribute.
 * Name: max-message-size
 * Value: Integer
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * max-message-size-value = 1*DIGIT ; DIGIT defined in RFC 4566
 *  Leading zeroes MUST NOT be used.
 * Example:
 * a=max-message-size:100000
 */
data class MaxMessageSizeAttribute internal constructor(
    var messageSize: Long,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(messageSize)
    }

    companion object {
        internal const val fieldName = "max-message-size"

        @JvmStatic
        fun of(messageSize: Long): MaxMessageSizeAttribute {
            return MaxMessageSizeAttribute(messageSize)
        }

        internal fun parse(value: String): SdpAttribute {
            val messageSize = value.toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $value as MaxMessageSizeAttribute")
            }
            return MaxMessageSizeAttribute(messageSize)
        }
    }
}
