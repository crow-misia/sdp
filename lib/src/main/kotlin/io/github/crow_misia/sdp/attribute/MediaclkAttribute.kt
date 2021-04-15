package io.github.crow_misia.sdp.attribute

data class MediaclkAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "mediaclk"

        @JvmStatic
        fun of(value: String): MediaclkAttribute {
            return MediaclkAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return MediaclkAttribute(value)
        }
    }
}