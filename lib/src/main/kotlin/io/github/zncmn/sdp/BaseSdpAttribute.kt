package io.github.zncmn.sdp

data class BaseSdpAttribute internal constructor(
    override var field: String,
    override var value: String?
) : SdpAttribute {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            value?.also {
                append(':')
                append(it)
            }
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(field: String, value: String? = null): BaseSdpAttribute {
            return BaseSdpAttribute(field, value)
        }

        @JvmStatic
        fun of(field: String, value: Int): BaseSdpAttribute {
            return BaseSdpAttribute(field, value.toString())
        }

        @JvmStatic
        fun parse(line: String): SdpAttribute {
            val colonIndex = line.indexOf(':', 2)
            val (field, value) = if (colonIndex < 0) {
                line.substring(2) to null
            } else {
                line.substring(2, colonIndex) to line.substring(colonIndex + 1)
            }

            return when (field) {
                "candidate" -> CandidateAttribute.parse(value)
                "control" -> ControlAttribute.parse(value)
                "cname" -> CNameAttribute.of(value)
                "fmtp" -> FormatAttribute.parse(value)
                "ice-lite" -> IceLiteAttribute.of()
                "rtcp" -> RTCPAttribute.parse(value)
                "rtpmap" -> RTPMapAttribute.parse(value)
                else -> of(field, value)
            }
        }
    }
}
