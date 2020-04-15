package io.github.zncmn.sdp

interface SdpAttribute : SdpElement {
    val field: String
    val value: String?
}