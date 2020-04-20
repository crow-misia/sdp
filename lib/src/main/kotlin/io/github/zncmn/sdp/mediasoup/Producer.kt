package io.github.zncmn.sdp.mediasoup

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ProducerCodecOptions(
    var opusStereo: Boolean,
    var opusFec: Boolean,
    var opusDtx: Boolean,
    var opusMaxPlaybackRate: Int,
    var opusPtime: Long,
    var videoGoogleStartBitrate: Long,
    var videoGoogleMaxBitrate: Long,
    var videoGoogleMinBitrate: Long
)
