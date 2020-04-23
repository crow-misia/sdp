package io.github.zncmn.sdp.attribute

data class MsidAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "msid"

        @JvmStatic
        fun of(vararg value: String): MsidAttribute {
            return MsidAttribute(value.joinToString(" "))
        }

        internal fun parse(value: String): SdpAttribute {
            return MsidAttribute(value)
        }
    }
}