package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class MediaclkAttribute internal constructor(
    override var value: String?
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "mediaclk"

        @JvmStatic
        fun of(value: String? = null): MediaclkAttribute {
            return MediaclkAttribute(value)
        }

        internal fun parse(value: String): MediaclkAttribute {
            return if (value.isEmpty())
                throw SdpParseException("could not parse: $value as MediaclkAttribute")
            else
                MediaclkAttribute(value)
        }
    }
}