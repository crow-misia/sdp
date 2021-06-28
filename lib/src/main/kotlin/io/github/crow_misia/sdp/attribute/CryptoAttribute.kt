package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class CryptoAttribute internal constructor(
    var id: Long,
    var suite: String,
    var config: String,
    var sessionConfig: String?,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(id)
        append(' ')
        append(suite)
        append(' ')
        append(config)
        sessionConfig?.also {
            append(' ')
            append(it)
        }
    }

    companion object {
        internal const val fieldName = "crypto"

        @JvmStatic @JvmOverloads
        fun of(id: Long, suite: String, config: String, sessionConfig: String? = null): CryptoAttribute {
            return CryptoAttribute(id, suite, config, sessionConfig)
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 4)
            val size = values.size
            if (size < 3) {
                throw SdpParseException("could not parse: $value as CryptoAttribute")
            }
            val id = values[0].toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $value as CryptoAttribute")
            }
            return CryptoAttribute(
                id = id,
                suite = values[1],
                config = values[2],
                sessionConfig = if (size < 4) null else values[3]
            )
        }
    }
}
