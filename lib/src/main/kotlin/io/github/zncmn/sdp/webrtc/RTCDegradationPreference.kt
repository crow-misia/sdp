package io.github.zncmn.sdp.webrtc

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class RTCDegradationPreference {
    @Json(name = "maintain-framerate") MAINTAIN_FRAMERATE,
    @Json(name = "maintain-resolution") MAINTAIN_RESOLUTION,
    @Json(name = "balanced") BALANCED
}