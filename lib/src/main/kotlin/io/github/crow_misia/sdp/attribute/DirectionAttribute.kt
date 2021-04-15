package io.github.crow_misia.sdp.attribute

sealed class DirectionAttribute(
    type: Direction
) : SdpAttribute {
    override val field: String = type.value

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

@Suppress("NOTHING_TO_INLINE")
object SendRecvAttribute : DirectionAttribute(Direction.SENDRECV) {
    @JvmStatic
    fun of() = this
}

object SendOnlyAttribute : DirectionAttribute(Direction.SENDONLY) {
    @JvmStatic
    fun of() = this
}

object RecvOnlyAttribute : DirectionAttribute(Direction.RECVONLY) {
    @JvmStatic
    fun of() = this
}

object InactiveAttribute : DirectionAttribute(Direction.INACTIVE) {
    @JvmStatic
    fun of() = this
}
