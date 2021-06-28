package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class ExtMapAttribute internal constructor(
    var id: Long,
    var direction: Direction?,
    var uri: String,
    var encryptUri: String?,
    var config: String?,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(id)
        direction?.also {
            append('/')
            append(it.value)
        }
        append(' ')
        append(uri)
        encryptUri?.also {
            append(' ')
            append(it)
        }
        config?.also {
            append(' ')
            append(it)
        }
    }

    companion object {
        internal const val fieldName = "extmap"

        @JvmStatic
        @JvmOverloads
        fun of(
            value: Long,
            direction: Direction? = null,
            uri: String,
            encryptUri: String? = null,
            config: String? = null,
        ): ExtMapAttribute {
            return ExtMapAttribute(value, direction, uri, encryptUri, config)
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ', limit = 4)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as ExtMapAttribute")
            }
            val tmp = values[0].split('/', limit = 2)
            val id = tmp[0].toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $value as ExtMapAttribute")
            }
            val direction = if (tmp.size > 1) {
                Direction.of(tmp[1])
            } else null

            return ExtMapAttribute(
                id = id,
                direction = direction,
                uri = values[1],
                encryptUri = if (size > 2) values[2] else null,
                config = if (size > 3) values[3] else null
            )
        }
    }
}
