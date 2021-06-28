package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SdpBandwidth internal constructor(
    var type: String,
    var bandwidth: Int
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append("b=")
        append(type)
        append(':')
        append(bandwidth)
        appendSdpLineSeparator()
    }

    companion object {
        @JvmStatic
        fun of(type: String, bandwidth: Int): SdpBandwidth {
            return SdpBandwidth(type, bandwidth)
        }

        internal fun parse(line: String): SdpBandwidth {
            val values = line.substring(2).split(':')
            if (values.size != 2) {
                throw SdpParseException("could not parse: $line as Bandwidth")
            }
            val bw = values[1].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $line as Bandwidth")
            }
            return SdpBandwidth(values[0], bw)
        }
    }
}
