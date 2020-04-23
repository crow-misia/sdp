package io.github.zncmn.sdp.attribute

data class IcePwdAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {
    companion object {
        internal const val FIELD_NAME = "ice-pwd"

        @JvmStatic
        fun of(value: String): IcePwdAttribute {
            return IcePwdAttribute(value.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}