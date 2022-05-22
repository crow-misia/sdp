package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.8. Bandwidth Information.
 * b=<bwtype>:<bandwidth>
 */
data class SdpBandwidth internal constructor(
    var bwtype: String,
    var bandwidth: Int,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        append(bwtype)
        append(':')
        append(bandwidth)
        appendSdpLineSeparator()
    }

    companion object {
        internal const val fieldPart = "b="

        @JvmStatic
        fun of(bwtype: String, bandwidth: Int): SdpBandwidth {
            return SdpBandwidth(
                bwtype = bwtype,
                bandwidth = bandwidth,
            )
        }

        internal fun parse(line: String): SdpBandwidth {
            val values = line.substring(2).split(':')
            if (values.size != 2) {
                throw SdpParseException("could not parse: $line as Bandwidth")
            }
            val bandwidth = values[1].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $line as Bandwidth")
            }
            return SdpBandwidth(
                bwtype = values[0],
                bandwidth = bandwidth,
            )
        }
    }
}
