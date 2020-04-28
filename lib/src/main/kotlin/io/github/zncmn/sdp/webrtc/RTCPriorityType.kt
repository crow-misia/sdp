package io.github.zncmn.sdp.webrtc

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class RTCPriorityType {
    @Json(name = "very-low") VERY_LOW,
    @Json(name = "low") LOW,
    @Json(name = "medium") MEDIUM,
    @Json(name = "high") HIGH
}