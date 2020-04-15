package io.github.zncmn.sdp

import java.lang.StringBuilder

class CNameAttribute @JvmOverloads constructor(
    var cname: String? = null
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
        fun parse(value: String?): CNameAttribute {
            return CNameAttribute(value)
        }
    }
}