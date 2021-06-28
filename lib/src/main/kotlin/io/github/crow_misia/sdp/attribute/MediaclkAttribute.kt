package io.github.crow_misia.sdp.attribute

data class MediaclkAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "mediaclk"

        @JvmStatic
        fun of(value: String): MediaclkAttribute {
            return MediaclkAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return MediaclkAttribute(value)
        }
    }
}
