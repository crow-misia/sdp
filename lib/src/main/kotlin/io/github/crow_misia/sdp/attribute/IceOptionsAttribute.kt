package io.github.crow_misia.sdp.attribute

data class IceOptionsAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "ice-options"

        @JvmStatic
        fun of(value: String): IceOptionsAttribute {
            return IceOptionsAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return IceOptionsAttribute(value)
        }
    }
}
