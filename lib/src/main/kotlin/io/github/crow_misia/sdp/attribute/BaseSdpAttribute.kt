package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.Utils

open class BaseSdpAttribute internal constructor(
    override val field: String,
    open var value: String,
) : SdpAttribute() {
    override fun equals(other: Any?): Boolean {
        if (other !is BaseSdpAttribute) {
            return false
        }
        return field == other.field && value == other.value
    }

    override fun hashCode(): Int {
        return field.hashCode() * 37 + value.hashCode()
    }

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        if (value.isNotEmpty()) {
            append(':')
            append(value)
        }
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(field: String, value: String? = null): BaseSdpAttribute {
            return BaseSdpAttribute(Utils.getFieldName(field), value.orEmpty())
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
