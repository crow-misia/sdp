package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class SsrcGroupAttribute internal constructor(
    var semantics: String,
    val ssrcs: MutableSet<Long>
) : SdpAttribute {
    override val field = FIELD_NAME

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        if (ssrcs.isEmpty()) {
            return
        }
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
            ssrcs.forEach {
                append(' ')
                append(it)
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "ssrc-group"

        @JvmStatic
        fun of(semantics: String, vararg ssrcs: Long): SsrcGroupAttribute {
            return SsrcGroupAttribute(semantics, ssrcs.toMutableSet())
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as SsrcGroupAttribute")
            }
            return SsrcGroupAttribute(values[0], values[1].splitToSequence(' ').map { it.toLong() }.toMutableSet())
        }
    }
}