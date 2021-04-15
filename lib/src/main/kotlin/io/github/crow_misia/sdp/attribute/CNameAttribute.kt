package io.github.crow_misia.sdp.attribute

data class CNameAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {

    override fun joinTo(buffer: StringBuilder) {
        if (value.isBlank()) {
            return
        }
        super.joinTo(buffer)
    }

    companion object {
        internal const val FIELD_NAME = "cname"

        @JvmStatic
        fun of(value: String): CNameAttribute {
            return CNameAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return CNameAttribute(value)
        }
    }
}