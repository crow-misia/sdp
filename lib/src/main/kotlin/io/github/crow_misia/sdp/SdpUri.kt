package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator
import java.net.URI

/**
 * RFC 8866 5.5. URI.
 * u=<uri>
 */
data class SdpUri internal constructor(
    var uri: URI,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        append(uri)
        appendSdpLineSeparator()
    }

    companion object {
        internal const val fieldPart = "u="

        @JvmStatic
        fun of(uri: String): SdpUri {
            return SdpUri(URI.create(uri))
        }

        internal fun parse(line: String): SdpUri {
            return of(uri = line.substring(2))
        }
    }
}
