package io.github.zncmn.sdp

data class SdpSessionName internal constructor(
    var name: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("s=")
            append(name)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(name: String? = null): SdpSessionName {
            return SdpSessionName(name.orEmpty().ifEmpty { " " })
        }

        internal fun parse(line: String): SdpSessionName {
            return of(line.substring(2))
        }
    }
}
