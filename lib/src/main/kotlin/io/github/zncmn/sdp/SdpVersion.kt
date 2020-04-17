package io.github.zncmn.sdp

data class SdpVersion internal constructor(
    var version: Int = 0
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("v=")
            append(version)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(version: Int = 0): SdpVersion {
            return SdpVersion(version)
        }

        internal fun parse(line: String): SdpVersion {
            return of(line.substring(2).toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $line as Version")
            })
        }
    }
}
