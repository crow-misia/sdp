package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpElement

interface SdpAttribute : SdpElement {
    val field: String
}