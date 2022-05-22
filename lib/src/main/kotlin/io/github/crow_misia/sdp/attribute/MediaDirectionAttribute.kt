package io.github.crow_misia.sdp.attribute

/**
 * RFC8866 6.7. Media Direction Attributes
 */
sealed class MediaDirectionAttribute(
    val type: Direction,
) : SdpAttribute() {
    override val field: String = type.value

    override fun valueJoinTo(buffer: StringBuilder) = buffer
}

/**
 * RFC8866 6.7.1. recvonly (Receive-Only)
 * Name: recvonly
 * Value:
 * Usage Level: media
 * Charset Dependent: no
 * Example:
 * a=recvonly
 */
object RecvOnlyAttribute : MediaDirectionAttribute(Direction.RECVONLY) {
    @JvmStatic
    fun of() = this
}

/**
 * RFC8866 6.7.2. sendrecv (Send-Receive)
 * Name: sendrecv
 * Value:
 * Usage Level: media
 * Charset Dependent: no
 * Example:
 * a=sendrecv
 */
object SendRecvAttribute : MediaDirectionAttribute(Direction.SENDRECV) {
    @JvmStatic
    fun of() = this
}

/**
 * RFC8866 6.7.3. sendonly (Send-Only)
 * Name: sendonly
 * Value:
 * Usage Level: media
 * Charset Dependent: no
 * Example:
 * a=sendonly
 */
object SendOnlyAttribute : MediaDirectionAttribute(Direction.SENDONLY) {
    @JvmStatic
    fun of() = this
}

/**
 * RFC8866 6.7.4. inactive
 * Name: inactive
 * Value:
 * Usage Level: media
 * Charset Dependent: no
 * Example:
 * a=inactive
 */
object InactiveAttribute : MediaDirectionAttribute(Direction.INACTIVE) {
    @JvmStatic
    fun of() = this
}
