package io.github.zncmn.sdp

data class EncryptionKey internal constructor(
    var method: String,
    var key: String?
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("k=")
            append(method)
            key?.also {
                append(':')
                append(it)
            }
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(method: String, key: String? = null): EncryptionKey {
            return EncryptionKey(method, key)
        }

        internal fun parse(line: String): EncryptionKey {
            val values = line.substring(2).split(':')
            val size = values.size
            if (size <= 0 || size > 2) {
                throw SdpParseException("could not parse: $line as EncryptionKey")
            }
            return of(values[0], if (size == 1) null else values[1])
        }
    }
}
