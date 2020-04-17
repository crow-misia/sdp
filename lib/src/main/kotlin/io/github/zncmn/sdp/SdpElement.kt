package io.github.zncmn.sdp

interface SdpElement {
    fun joinTo(buffer: StringBuilder)
}