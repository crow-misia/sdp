package io.github.zncmn.sdp.attribute

sealed class DirectionAttribute(
    type: Direction
) : SdpAttribute {
    override val field: String = type.name.toLowerCase()
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
}

object SendRecvAttribute : DirectionAttribute(Direction.SENDRECV) {
    val FIELD_NAME = Direction.SENDRECV.name.toLowerCase()

    @JvmStatic
    fun of() = this
}

object SendOnlyAttribute : DirectionAttribute(Direction.SENDONLY) {
    val FIELD_NAME = Direction.SENDONLY.name.toLowerCase()

    @JvmStatic
    fun of() = this
}

object RecvOnlyAttribute : DirectionAttribute(Direction.RECVONLY) {
    val FIELD_NAME = Direction.RECVONLY.name.toLowerCase()

    @JvmStatic
    fun of() = this
}

object InactiveAttribute : DirectionAttribute(Direction.INACTIVE) {
    val FIELD_NAME = Direction.INACTIVE.name.toLowerCase()

    @JvmStatic
    fun of() = this
}
