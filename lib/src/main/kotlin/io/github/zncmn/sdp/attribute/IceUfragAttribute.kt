package io.github.zncmn.sdp.attribute

data class IceUfragAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {
    companion object {
        internal const val FIELD_NAME = "ice-ufrag"

        @JvmStatic
        fun of(value: String): IceUfragAttribute {
            return IceUfragAttribute(value.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}