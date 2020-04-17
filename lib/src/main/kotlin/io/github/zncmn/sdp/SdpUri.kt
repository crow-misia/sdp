package io.github.zncmn.sdp

data class SdpUri internal constructor(
    var uri: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("u=")
            append(uri)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun of(uri: String): SdpUri {
            return SdpUri(uri)
        }

        internal fun parse(line: String): SdpUri {
            return of(line.substring(2))
        }
    }
}
