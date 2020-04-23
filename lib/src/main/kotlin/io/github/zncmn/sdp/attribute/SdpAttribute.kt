package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpElement

interface SdpAttribute : SdpElement {
    val field: String
}