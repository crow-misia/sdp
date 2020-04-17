package io.github.zncmn.sdp

data class RTPMapAttribute internal constructor(
    var payloadType: Int,
    var encodingName: String,
    var clockRate: Int,
    var encodingParameters: String?
) : SdpAttribute {
    override val field = FIELD_NAME
    override val value: String
        get() = buildString { valueJoinTo(this) }

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append(':')
            valueJoinTo(this)
            append("\r\n")
        }
    }

    private fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
            append(payloadType)
            append(' ')
            append(encodingName)
            append('/')
            append(clockRate)
            encodingParameters?.also {
                append('/')
                append(it)
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "rtpmap"

        @JvmStatic @JvmOverloads
        fun of(payloadType: Int,
               encodingName: String,
               clockRate: Int,
               encodingParameters: String? = null
        ): RTPMapAttribute {
            return RTPMapAttribute(payloadType, encodingName, clockRate, encodingParameters)
        }

        internal fun parse(value: String?): RTPMapAttribute {
            value ?: run {
                throw SdpParseException("could not parse: $value as RTPMapAttribute")
            }
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as RTPMapAttribute")
            }
            val payloadType = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: [${values[0]}] as RTPMapAttribute")
            }

            val parameters = values[1].split('/', limit = 3)
            val paramsSize = parameters.size
            if (paramsSize < 2) {
                throw SdpParseException("could not parse: $value as RTPMapAttribute")
            }
            val clockRate = parameters[1].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as RTPMapAttribute")
            }
            return RTPMapAttribute(payloadType, parameters[0], clockRate, if (paramsSize > 2) parameters[2] else null)
        }
    }
}