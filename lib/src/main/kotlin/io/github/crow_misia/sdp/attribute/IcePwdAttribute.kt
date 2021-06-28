package io.github.crow_misia.sdp.attribute

data class IcePwdAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "ice-pwd"

        @JvmStatic
        fun of(value: String): IcePwdAttribute {
            return IcePwdAttribute(value.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}
