package io.github.zncmn.sdp.attribute

data class IceOptionsAttribute internal constructor(
    override var value: String?
) : BaseSdpAttribute(FIELD_NAME, value) {
    companion object {
        internal const val FIELD_NAME = "ice-options"

        @JvmStatic
        fun of(value: String): IceOptionsAttribute {
            return IceOptionsAttribute(value)
        }

        internal fun parse(value: String): IceOptionsAttribute {
            return IceOptionsAttribute(value)
        }
    }
}