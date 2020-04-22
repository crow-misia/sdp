package io.github.zncmn.sdp.mediasoup

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProducerCodecOptions(
    var opusStereo: Boolean? = null,
    var opusFec: Boolean? = null,
    var opusDtx: Boolean? = null,
    var opusMaxPlaybackRate: Int? = null,
    var opusPtime: Long? = null,
    var videoGoogleStartBitrate: Int? = null,
    var videoGoogleMaxBitrate: Int? = null,
    var videoGoogleMinBitrate: Int? = null
)
