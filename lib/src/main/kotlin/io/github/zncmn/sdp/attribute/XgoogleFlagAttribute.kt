package io.github.zncmn.sdp.attribute

data class XgoogleFlagAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {
    companion object {
        internal const val FIELD_NAME = "x-google-flag"

        @JvmStatic
        fun of(type: String): XgoogleFlagAttribute {
            return XgoogleFlagAttribute(type)
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}
