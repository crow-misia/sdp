package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SdpVersion internal constructor(
    var version: Int = 0
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append("v=")
        append(version)
        appendSdpLineSeparator()
    }

    companion object {
        private val ZERO = SdpVersion(0)

        @JvmStatic @JvmOverloads
        fun of(version: Int = 0): SdpVersion {
            return if (version == 0) ZERO else SdpVersion(version)
        }

        internal fun parse(line: String): SdpVersion {
            val version = line.substring(2).toIntOrNull()
                ?: throw SdpParseException("could not parse: $line as Version")
            return of(version)
        }
    }
}
