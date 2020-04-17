package io.github.zncmn.sdp.attribute

data class CNameAttribute internal constructor(
    override var value: String?
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "cname"

        @JvmStatic
        fun of(value: String? = null): CNameAttribute {
            return CNameAttribute(value)
        }

        internal fun parse(value: String): CNameAttribute {
            return if (value.isEmpty())
                CNameAttribute(null)
            else
                CNameAttribute(value)
        }
    }
}