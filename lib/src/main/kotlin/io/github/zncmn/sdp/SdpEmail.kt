package io.github.zncmn.sdp

data class SdpEmail internal constructor(
    var address: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("e=")
            append(address)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun of(address: String): SdpEmail {
            return SdpEmail(address)
        }

        internal fun parse(line: String): SdpEmail {
            return SdpEmail(line.substring(2))
        }
    }
}
