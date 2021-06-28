package io.github.crow_misia.sdp.attribute

data class MsidAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "msid"

        @JvmStatic
        fun of(vararg value: String): MsidAttribute {
            return MsidAttribute(value.joinToString(" "))
        }

        internal fun parse(value: String): SdpAttribute {
            return MsidAttribute(value)
        }
    }
}
