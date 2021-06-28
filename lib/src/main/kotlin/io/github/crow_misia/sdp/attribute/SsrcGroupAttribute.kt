package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class SsrcGroupAttribute internal constructor(
    var semantics: String,
    var ssrcs: MutableList<Long>,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder): StringBuilder {
        if (ssrcs.isEmpty()) {
            return buffer
        }
        return super.joinTo(buffer)
    }

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(semantics)
        ssrcs.forEach {
            append(' ')
            append(it)
        }
    }

    companion object {
        internal const val fieldName = "ssrc-group"

        @JvmStatic
        fun of(semantics: String, ssrcs: List<Long>): SsrcGroupAttribute {
            return SsrcGroupAttribute(semantics, ssrcs.toMutableList())
        }

        @JvmStatic
        fun of(semantics: String, vararg ssrcs: Long): SsrcGroupAttribute {
            return SsrcGroupAttribute(semantics, ssrcs.toMutableList())
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as SsrcGroupAttribute")
            }
            return SsrcGroupAttribute(values[0], values[1].splitToSequence(' ').map { it.toLong() }.toMutableList())
        }
    }
}
