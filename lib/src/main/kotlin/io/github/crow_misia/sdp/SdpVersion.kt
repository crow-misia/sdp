package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.1. Protocol Version.
 * v=0
 */
data class SdpVersion internal constructor(
    val version: Int,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(lineType)
        append(version)
        appendSdpLineSeparator()
    }

    companion object {
        internal const val lineType = "v="

        private val ZERO = SdpVersion(0)

        @JvmStatic
        @JvmOverloads
        fun of(version: Int = 0): SdpVersion {
            return if (version == 0) ZERO else SdpVersion(version)
        }

        internal fun parse(line: String): SdpVersion {
            val version = line.substring(2).toIntOrNull()
                ?: throw SdpParseException("could not parse: $line as Version")
            return of(version = version)
        }
    }
}
