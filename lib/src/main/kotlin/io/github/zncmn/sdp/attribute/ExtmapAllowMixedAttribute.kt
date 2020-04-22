package io.github.zncmn.sdp.attribute

data class ExtmapAllowMixedAttribute internal constructor(
    override var value: String?
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "extmap-allow-mixed"

        @JvmStatic
        fun of(value: String? = null): ExtmapAllowMixedAttribute {
            return ExtmapAllowMixedAttribute(value)
        }

        internal fun parse(value: String): ExtmapAllowMixedAttribute {
            return if (value.isEmpty())
                ExtmapAllowMixedAttribute(null)
            else
                ExtmapAllowMixedAttribute(value)
        }
    }
}