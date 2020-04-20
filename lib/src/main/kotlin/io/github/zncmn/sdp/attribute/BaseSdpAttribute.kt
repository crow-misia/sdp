package io.github.zncmn.sdp.attribute

open class BaseSdpAttribute internal constructor(
    override var field: String,
    override var value: String?
) : SdpAttribute {
    override fun equals(other: Any?): Boolean {
        if (other !is BaseSdpAttribute) {
            return false
        }
        return field == other.field && value == other.value
    }

    override fun hashCode(): Int {
        return field.hashCode() * 37 + value.hashCode()
    }

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
        @JvmStatic @JvmOverloads
        fun of(field: String, value: String = ""): BaseSdpAttribute {
            return BaseSdpAttribute(SdpAttribute.getFieldName(field), if (value.isEmpty()) null else value)
        }

        @JvmStatic
        fun of(field: String, value: Int): BaseSdpAttribute {
            return of(field, value.toString())
        }

        @JvmStatic
        fun of(field: String, value: Long): BaseSdpAttribute {
            return of(field, value.toString())
        }
    }
}
