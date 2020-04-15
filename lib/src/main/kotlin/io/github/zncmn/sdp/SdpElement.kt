package io.github.zncmn.sdp

import java.lang.StringBuilder

interface SdpElement {
    fun joinTo(buffer: StringBuilder)
}