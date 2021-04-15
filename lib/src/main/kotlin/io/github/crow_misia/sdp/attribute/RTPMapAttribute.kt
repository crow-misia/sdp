package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class RTPMapAttribute internal constructor(
    var payloadType: Int,
    var encodingName: String,
    var clockRate: Int?,
    var encodingParameters: String?
) : SdpAttribute {
    override val field = FIELD_NAME

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
            encodingParameters?.also {
                append('/')
                append(clockRate ?: 0)
                append('/')
                append(it)
            } ?: clockRate?.also {
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
               clockRate: Int? = null,
               encodingParameters: String? = null
        ): RTPMapAttribute {
            return RTPMapAttribute(
                payloadType = payloadType,
                encodingName = encodingName,
                clockRate = clockRate,
                encodingParameters = encodingParameters
            )
        }

        internal fun parse(value: String): SdpAttribute {
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
            val clockRate = if (paramsSize > 1) {
                parameters[1].toIntOrNull() ?: run {
                    throw SdpParseException("could not parse: $value as RTPMapAttribute")
                }
            } else null

            return RTPMapAttribute(
                payloadType = payloadType,
                encodingName = parameters[0],
                clockRate = clockRate,
                encodingParameters = if (paramsSize > 2) parameters[2] else null
            )
        }
    }
}