package io.github.crow_misia.sdp.attribute

data class MidAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "mid"

        @JvmStatic
        fun of(value: String): MidAttribute {
            return MidAttribute(value.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}
