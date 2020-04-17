package io.github.zncmn.sdp

data class SdpSessionInformation internal constructor(
    var description: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("i=")
            append(description)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun of(description: String): SdpSessionInformation {
            return SdpSessionInformation(description)
        }

        internal fun parse(line: String): SdpSessionInformation {
            return SdpSessionInformation(line.substring(2))
        }
    }
}
