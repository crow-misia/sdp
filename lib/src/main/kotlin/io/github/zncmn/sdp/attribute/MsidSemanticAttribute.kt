package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class MsidSemanticAttribute internal constructor(
    var semantic: String,
    var token: String
) : SdpAttribute {
    override val field = FIELD_NAME

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append(':')
            valueJoinTo(this)
            append("\r\n")
        }
    }

    private fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
            append(' ')
            append(semantic)
            append(' ')
            append(token)
        }
    }

    companion object {
        internal const val FIELD_NAME = "msid-semantic"

        @JvmStatic
        fun of(semantic: String, token: String): MsidSemanticAttribute {
            return MsidSemanticAttribute(semantic, token)
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.trimStart().split(' ', limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as MsidSemanticAttribute")
            }
            return MsidSemanticAttribute(values[0], values[1])
        }
    }
}