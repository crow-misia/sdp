package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8866 6.6. rtpmap
 * Name: rtpmap
 * Value: rtpmap-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * rtpmap-value = payload-type SP encoding-name "/" clock-rate [ "/" encoding-params ]
 * payload-type = zero-based-integer
 * encoding-name = token
 * clock-rate = integer
 * encoding-params = channels
 * channels = integer
 * Example:
 * a=maxptime:20
 */
data class RTPMapAttribute internal constructor(
    var payloadType: Int,
    var encodingName: String,
    var clockRate: Int?,
    var encodingParameters: Int?,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(payloadType)
        append(' ')
        append(encodingName)
        clockRate?.also {
            append('/')
            append(it)
        }
        encodingParameters?.also {
            append('/')
            append(it)
        }
    }

    companion object {
        internal const val fieldName = "rtpmap"

        @JvmStatic
        @JvmOverloads
        fun of(
            payloadType: Int,
            encodingName: String,
            clockRate: Int? = null,
            encodingParameters: Int? = null,
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

            val encodingParameters = if (paramsSize > 2) {
                parameters[2].toIntOrNull() ?: run {
                    throw SdpParseException("could not parse: $value as RTPMapAttribute")
                }
            } else null

            return RTPMapAttribute(
                payloadType = payloadType,
                encodingName = parameters[0],
                clockRate = clockRate,
                encodingParameters = encodingParameters,
            )
        }
    }
}
