package io.github.zncmn.sdp

data class CNameAttribute internal constructor(
    var cname: String?
) : SdpAttribute {
    override val field = "cname"
    override val value: String?
        get() = cname

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            cname?.also {
                append(':')
                append(it)
            }
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun of(value: String? = null): CNameAttribute {
            return CNameAttribute(value)
        }
    }
}