package io.github.crow_misia.sdp.attribute

data class IceUfragAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "ice-ufrag"

        @JvmStatic
        fun of(value: String): IceUfragAttribute {
            return IceUfragAttribute(value.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}
