package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class SsrcGroupAttribute internal constructor(
    var semantics: String,
    var ssrcs: String
) : SdpAttribute {
    override val field = FIELD_NAME
    override val value: String
        get() = buildString { valueJoinTo(this) }

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
            append(semantics)
            append(' ')
            append(ssrcs)
        }
    }

    companion object {
        internal const val FIELD_NAME = "ssrc-group"

        @JvmStatic
        fun of(semantics: String, ssrcs: String): SsrcGroupAttribute {
            return SsrcGroupAttribute(semantics, ssrcs)
        }

        internal fun parse(value: String): SsrcGroupAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as SsrcGroupAttribute")
            }
            return SsrcGroupAttribute(values[0], values[1])
        }
    }
}