package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class FingerprintAttribute internal constructor(
    var type: String,
    var hash: String
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
            append(type)
            append(' ')
            append(hash)
        }
    }

    companion object {
        internal const val FIELD_NAME = "fingerprint"

        @JvmStatic
        fun of(type: String, hash: String): FingerprintAttribute {
            return FingerprintAttribute(type, hash)
        }

        internal fun parse(value: String): FingerprintAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size != 2) {
                throw SdpParseException("could not parse: $value as FingerprintAttribute")
            }
            return FingerprintAttribute(values[0], values[1])
        }
    }
}