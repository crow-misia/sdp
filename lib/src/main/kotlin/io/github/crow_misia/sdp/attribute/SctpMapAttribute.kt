package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseContext
import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils.splitOnSpaces

data class SctpMapAttribute internal constructor(
    var sctpmapNumber: Int,
    var app: String,
    var maxMessageSize: Int?,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(sctpmapNumber)
        append(' ')
        append(app)
        maxMessageSize?.also {
            append(' ')
            append(it)
        }
    }

    companion object {
        internal const val fieldName = "sctpmap"

        @JvmStatic @JvmOverloads
        fun of(sctpmapNumber: Int, app: String, maxMessageSize: Int? = null): SctpMapAttribute {
            return SctpMapAttribute(sctpmapNumber, app, maxMessageSize)
        }

        context(_: SdpParseContext)
        internal fun parse(value: String): SdpAttribute {
            val values = value.splitOnSpaces(limit = 3)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as SctpMapAttribute")
            }
            val sctpmapNumber = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as SctpMapAttribute")
            }
            val maxMessageSize = if (size > 2) values[2].toIntOrNull() else null
            return SctpMapAttribute(sctpmapNumber, values[1], maxMessageSize)
        }
    }
}
