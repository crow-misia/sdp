package io.github.zncmn.sdp

class IceLiteAttribute private constructor(): SdpAttribute {
    override val field = FIELD_NAME
    override val value: String? = null

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append("\r\n")
        }
    }

    companion object {
        internal const val FIELD_NAME = "ice-lite"

        private val INSTANCE = IceLiteAttribute()

        @JvmStatic
        fun of() = INSTANCE
    }
}