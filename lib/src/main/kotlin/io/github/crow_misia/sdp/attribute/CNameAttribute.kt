package io.github.crow_misia.sdp.attribute

data class CNameAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder): StringBuilder {
        if (value.isBlank()) {
            return buffer
        }
        return super.joinTo(buffer)
    }

    companion object {
        internal const val fieldName = "cname"

        @JvmStatic
        fun of(value: String): CNameAttribute {
            return CNameAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return CNameAttribute(value)
        }
    }
}
