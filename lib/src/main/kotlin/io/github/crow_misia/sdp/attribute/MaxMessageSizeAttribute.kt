package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

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
