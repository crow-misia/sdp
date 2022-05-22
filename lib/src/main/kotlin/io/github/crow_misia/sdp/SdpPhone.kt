package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.6. Phone Number.
 * p=<phone-number>
 */
data class SdpPhone internal constructor(
    var number: String,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        append(number)
        appendSdpLineSeparator()
    }

    companion object {
        internal const val fieldPart = "p="

        @JvmStatic
        fun of(number: String): SdpPhone {
            return SdpPhone(number = number)
        }

        internal fun parse(line: String): SdpPhone {
            return SdpPhone(number = line.substring(2))
        }
    }
}
