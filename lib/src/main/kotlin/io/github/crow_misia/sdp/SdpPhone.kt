package io.github.crow_misia.sdp

data class SdpPhone internal constructor(
    var number: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("p=")
            append(number)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun of(number: String): SdpPhone {
            return SdpPhone(number)
        }

        internal fun parse(line: String): SdpPhone {
            return SdpPhone(line.substring(2))
        }
    }
}
