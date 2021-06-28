package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

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
