package io.github.zncmn.sdp

class IceLiteAttribute private constructor(): SdpAttribute {
    override val field = "ice-lite"
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
        private val INSTANCE = IceLiteAttribute()

        @JvmStatic
        fun of() = INSTANCE
    }
}