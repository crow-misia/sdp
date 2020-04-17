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
            return BaseSdpAttribute(SdpAttribute.getFieldName(field), value)
        }

        @JvmStatic
        fun of(field: String, value: Int): BaseSdpAttribute {
            return BaseSdpAttribute(SdpAttribute.getFieldName(field), value.toString())
        }

        @JvmStatic
        fun parse(line: String): SdpAttribute {
            val colonIndex = line.indexOf(':', 2)
            val (field, value) = if (colonIndex < 0) {
                line.substring(2) to null
            } else {
                line.substring(2, colonIndex) to line.substring(colonIndex + 1)
            }

            return when (val lowerField = SdpAttribute.getFieldName(field)) {
                CandidateAttribute.FIELD_NAME -> CandidateAttribute.parse(value)
                ControlAttribute.FIELD_NAME -> ControlAttribute.parse(value)
                CNameAttribute.FIELD_NAME -> CNameAttribute.of(value)
                FormatAttribute.FIELD_NAME -> FormatAttribute.parse(value)
                IceLiteAttribute.FIELD_NAME -> IceLiteAttribute.of()
                RTCPAttribute.FIELD_NAME -> RTCPAttribute.parse(value)
                RTPMapAttribute.FIELD_NAME -> RTPMapAttribute.parse(value)
                else -> BaseSdpAttribute(lowerField, value)
            }
        }
    }
}
