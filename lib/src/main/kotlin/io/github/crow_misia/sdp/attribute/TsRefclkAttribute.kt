package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class TsRefclkAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "ts-refclk"

        @JvmStatic
        fun of(value: String): TsRefclkAttribute {
            return TsRefclkAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return if (value.isEmpty())
                throw SdpParseException("could not parse: $value as TsRefclkAttribute")
            else
                TsRefclkAttribute(value)
        }
    }
}
