package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class EncryptionKey internal constructor(
    var method: Method,
    var key: String?
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append("k=")
        append(method.value)
        key?.also {
            append(':')
            append(it)
        }
        appendSdpLineSeparator()
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(method: Method, key: String? = null): EncryptionKey {
            return EncryptionKey(method, key)
        }

        internal fun parse(line: String): EncryptionKey {
            val values = line.substring(2).split(':')
            val size = values.size
            if (size <= 0 || size > 2) {
                throw SdpParseException("could not parse: $line as EncryptionKey")
            }
            val method = Method.of(values[0]) ?: run {
                throw SdpParseException("unsupported method ${values[0]} as EncryptionKey")
            }
            return EncryptionKey(method, if (size == 1) null else values[1])
        }
    }

    enum class Method {
        CLEAR,
        PROMPT,
        BASE64,
        URI
        ;
        internal val value = Utils.getName(name)

        companion object {
            private val MAPPING = values().associateBy { it.value }

            @JvmStatic
            fun of(value: String): Method? = MAPPING[value]
        }
    }
}
