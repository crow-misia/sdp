package io.github.crow_misia.sdp.attribute

data class XgoogleFlagAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "x-google-flag"

        @JvmStatic
        fun of(type: String): XgoogleFlagAttribute {
            return XgoogleFlagAttribute(type)
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}
