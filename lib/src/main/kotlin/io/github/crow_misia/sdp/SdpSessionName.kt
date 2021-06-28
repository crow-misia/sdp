package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

data class SdpSessionName internal constructor(
    var name: String,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        append(name)
        appendSdpLineSeparator()
    }

    companion object {
        internal const val fieldPart = "s="

        @JvmStatic
        @JvmOverloads
        fun of(name: String? = null): SdpSessionName {
            return SdpSessionName(name.orEmpty().ifEmpty { "-" })
        }

        internal fun parse(line: String): SdpSessionName {
            return SdpSessionName(line.substring(2))
        }
    }
}
