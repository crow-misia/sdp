package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseContext
import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils.splitOnSpaces

data class CryptoAttribute internal constructor(
    var id: Long,
    var suite: String,
    var config: String,
    var sessionConfig: String?,
) : SdpAttribute() {
    override val field = FIELD_NAME

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
        internal const val FIELD_NAME = "crypto"

        @JvmStatic @JvmOverloads
        fun of(id: Long, suite: String, config: String, sessionConfig: String? = null): CryptoAttribute {
            return CryptoAttribute(id, suite, config, sessionConfig)
        }

        context(_: SdpParseContext)
        internal fun parse(value: String): SdpAttribute {
            val values = value.splitOnSpaces(limit = 4)
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
