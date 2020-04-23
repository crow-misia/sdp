package io.github.zncmn.sdp.attribute

data class MidAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {
    companion object {
        internal const val FIELD_NAME = "mid"

        @JvmStatic
        fun of(value: String): MidAttribute {
            return MidAttribute(value.trimStart())
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }
}