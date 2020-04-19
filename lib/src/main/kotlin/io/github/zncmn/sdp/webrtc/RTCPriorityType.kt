package io.github.zncmn.sdp.webrtc

import com.squareup.moshi.Json

enum class RTCPriorityType {
    @Json(name = "very-low") VERY_LOW,
    @Json(name = "low") LOW,
    @Json(name = "medium") MEDIUM,
    @Json(name = "high") HIGH
}