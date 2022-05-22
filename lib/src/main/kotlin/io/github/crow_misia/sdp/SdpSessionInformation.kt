package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.4. Session Information.
 * i=<session information>
 */
data class SdpSessionInformation internal constructor(
    var description: String,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(lineType)
        append(description)
        appendSdpLineSeparator()
    }

    companion object {
        internal const val lineType = "i="

        @JvmStatic
        fun of(description: String): SdpSessionInformation {
            return SdpSessionInformation(description = description)
        }

        internal fun parse(line: String): SdpSessionInformation {
            return SdpSessionInformation(description = line.substring(2))
        }
    }
}
