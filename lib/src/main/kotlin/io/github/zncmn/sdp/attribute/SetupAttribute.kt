package io.github.zncmn.sdp.attribute

data class SetupAttribute internal constructor(
    override var value: String?
) : BaseSdpAttribute(FIELD_NAME, value) {
    companion object {
        internal const val FIELD_NAME = "setup"

        @JvmStatic
        fun of(type: Type): SetupAttribute {
            return SetupAttribute(type.name.toLowerCase())
        }

        internal fun parse(value: String): SetupAttribute {
            return SetupAttribute(value)
        }
    }

    enum class Type {
        ACTPASS,
        ACTIVE,
        PASSIVE
    }
}
