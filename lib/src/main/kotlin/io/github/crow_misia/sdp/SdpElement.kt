package io.github.crow_misia.sdp

interface SdpElement {
    fun joinTo(buffer: StringBuilder)
}