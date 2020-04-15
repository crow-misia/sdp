package io.github.zncmn.sdp

import java.lang.StringBuilder

open class BaseSdpAttribute @JvmOverloads constructor(
    override var field: String,
    override var value: String? = null
) : SdpAttribute {
    constructor(field: String, value: Int) : this(field, value.toString())

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            value?.also {
                append(':')
                append(it)
            }
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): SdpAttribute {
            val colonIndex = line.indexOf(':', 2)
            val (field, value) = if (colonIndex < 0) {
                line.substring(2) to null
            } else {
                line.substring(2, colonIndex) to line.substring(colonIndex + 1)
            }

            return when (field) {
                "candidate" -> CandidateAttribute.parse(value)
                "cname" -> CNameAttribute.parse(value)
                "fmtp" -> FormatAttribute.parse(value)
                "rtpmap" -> RTPMapAttribute.parse(value)
                else -> BaseSdpAttribute(field, value)
            }
        }
    }
}
