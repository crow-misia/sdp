package io.github.crow_misia.sdp

abstract class SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    abstract fun joinTo(buffer: StringBuilder): StringBuilder
}
