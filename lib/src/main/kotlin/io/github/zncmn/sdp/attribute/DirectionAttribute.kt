package io.github.zncmn.sdp.attribute

sealed class DirectionAttribute(
    override val field: String
) : SdpAttribute {
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
        internal const val FIELD_NAME_SENDRECV = "sendrecv"
        internal const val FIELD_NAME_SENDONLY = "sendonly"
        internal const val FIELD_NAME_RECVONLY = "recvonly"
        internal const val FIELD_NAME_INACTIVE = "inactive"
    }
}

object SendRecvAttribute : DirectionAttribute(FIELD_NAME_SENDRECV) {
    @JvmStatic
    fun of() = this
}

object SendOnlyAttribute : DirectionAttribute(FIELD_NAME_SENDONLY) {
    @JvmStatic
    fun of() = this
}

object RecvOnlyAttribute : DirectionAttribute(FIELD_NAME_RECVONLY) {
    @JvmStatic
    fun of() = this
}

object InactiveAttribute : DirectionAttribute(FIELD_NAME_INACTIVE) {
    @JvmStatic
    fun of() = this
}
