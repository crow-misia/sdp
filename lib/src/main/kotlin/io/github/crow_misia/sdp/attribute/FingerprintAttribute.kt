package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class FingerprintAttribute internal constructor(
    var type: String,
    var hash: String,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(type)
        append(' ')
        append(hash)
    }

    companion object {
        internal const val fieldName = "fingerprint"

        @JvmStatic
        fun of(type: String, hash: String): FingerprintAttribute {
            return FingerprintAttribute(type.trim(), hash.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size != 2) {
                throw SdpParseException("could not parse: $value as FingerprintAttribute")
            }
            return of(values[0], values[1])
        }
    }
}
