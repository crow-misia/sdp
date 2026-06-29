package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseContext
import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils.splitOnSpaces

data class MsidSemanticAttribute internal constructor(
    var semantic: String,
    var token: String,
) : SdpAttribute() {
    override val field = FIELD_NAME

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(": ")
        append(semantic)
        if (token.isNotEmpty()) {
            append(' ')
            append(token)
        }
    }

    companion object {
        internal const val FIELD_NAME = "msid-semantic"

        @JvmStatic @JvmOverloads
        fun of(semantic: String, token: String? = null): MsidSemanticAttribute {
            return MsidSemanticAttribute(semantic, token.orEmpty())
        }

        context(_: SdpParseContext)
        internal fun parse(value: String): SdpAttribute {
            val values = value.splitOnSpaces(limit = 2)
            if (values.isEmpty()) {
                throw SdpParseException("could not parse: $value as MsidSemanticAttribute")
            }
            return MsidSemanticAttribute(values[0], if (values.size > 1) values[1] else "")
        }
    }
}
