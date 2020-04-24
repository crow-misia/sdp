package io.github.zncmn.sdp.mediasoup

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.squareup.moshi.Moshi
import org.junit.jupiter.api.Test

internal class RtpParametersTest {
    private val moshi = Moshi.Builder().build()

    // "rtpParameters":{"codecs":[{"mimeType":"audio\/opus","payloadType":100,"clockRate":48000,"channels":2,"parameters":{"minptime":10,"useinbandfec":1,"sprop-stereo":1,"usedtx":1},"rtcpFeedback":[]}],"headerExtensions":[{"uri":"urn:ietf:params:rtp-hdrext:sdes:mid","id":1,"encrypt":false,"parameters":{}},{"uri":"http:\/\/www.webrtc.org\/experiments\/rtp-hdrext\/abs-send-time","id":4,"encrypt":false,"parameters":{}},{"uri":"urn:ietf:params:rtp-hdrext:ssrc-audio-level","id":10,"encrypt":false,"parameters":{}}],"encodings":[{"ssrc":980715670}],"rtcp":{"cname":"gJYTQGm9n8z+u1ql","reducedSize":true,"mux":true},"mid":"0"}

    @Test
    fun serializeRtpParameters() {
        val adapter = moshi.adapter(RtpParameters::class.java)
        assertThat(adapter.toJson(RtpParameters(
            mid = "0",
            codecs = listOf(
                RtpCodecParameters(
                    mimeType = "audio/opus",
                    payloadType = 100,
                    clockRate = 48000,
                    channels = 2,
                    parameters = mapOf(
                        "minptime" to 10,
                        "useinbandfec" to 1,
                        "sprop-stereo" to 1,
                        "usedtx" to 1
                    ),
                    rtcpFeedback = emptyList()
                )
            ),
            headerExtensions = listOf(
                RtpHeaderExtensionParameters(
                    id = 1,
                    uri = "urn:ietf:params:rtp-hdrext:sdes:mid",
                    encrypt = false,
                    parameters = emptyMap<String, Any>()

                ),
                RtpHeaderExtensionParameters(
                    id = 4,
                    uri = "http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time",
                    encrypt = false,
                    parameters = emptyMap<String, Any>()
                ),
                RtpHeaderExtensionParameters(
                    id = 10,
                    uri = "urn:ietf:params:rtp-hdrext:ssrc-audio-level",
                    encrypt = false,
                    parameters = emptyMap<String, Any>()
                )
            ),
            encodings = listOf(
                RtpEncodingParameters(
                    ssrc = 980715670
                )
            ),
            rtcp = RtcpParameters(
                cname = "gJYTQGm9n8z+u1ql",
                reducedSize = true,
                mux = true
            )
        ))).isEqualTo("""
            {"mid":"0","codecs":[{"mimeType":"audio/opus","payloadType":100,"clockRate":48000,"channels":2,"parameters":{"minptime":10,"useinbandfec":1,"sprop-stereo":1,"usedtx":1},"rtcpFeedback":[]}],"headerExtensions":[{"uri":"urn:ietf:params:rtp-hdrext:sdes:mid","id":1,"encrypt":false,"parameters":{}},{"uri":"http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time","id":4,"encrypt":false,"parameters":{}},{"uri":"urn:ietf:params:rtp-hdrext:ssrc-audio-level","id":10,"encrypt":false,"parameters":{}}],"encodings":[{"ssrc":980715670,"dtx":false}],"rtcp":{"cname":"gJYTQGm9n8z+u1ql","reducedSize":true,"mux":true}}
        """.trimIndent())
    }

    @Test
    fun deserializeRtpParameters() {
        val adapter = moshi.adapter(RtpParameters::class.java)
        assertThat(adapter.fromJson("""
            {"codecs":[{"mimeType":"audio/opus","payloadType":100,"clockRate":48000,"channels":2,"parameters":{"minptime":10,"useinbandfec":1,"sprop-stereo":1,"usedtx":1},"rtcpFeedback":[]}],"headerExtensions":[{"uri":"urn:ietf:params:rtp-hdrext:sdes:mid","id":1,"encrypt":false,"parameters":{}},{"uri":"http:\/\/www.webrtc.org\/experiments\/rtp-hdrext\/abs-send-time","id":4,"encrypt":false,"parameters":{}},{"uri":"urn:ietf:params:rtp-hdrext:ssrc-audio-level","id":10,"encrypt":false,"parameters":{}}],"encodings":[{"ssrc":980715670,"priority":1.0}],"rtcp":{"cname":"gJYTQGm9n8z+u1ql","reducedSize":true,"mux":true},"mid":"0"}
        """.trimIndent())).isEqualTo(RtpParameters(
            mid = "0",
            codecs = listOf(
                RtpCodecParameters(
                    mimeType = "audio/opus",
                    payloadType = 100,
                    clockRate = 48000,
                    channels = 2,
                    parameters = mapOf(
                        "minptime" to 10.0,
                        "useinbandfec" to 1.0,
                        "sprop-stereo" to 1.0,
                        "usedtx" to 1.0
                    ),
                    rtcpFeedback = emptyList()
                )
            ),
            headerExtensions = listOf(
                RtpHeaderExtensionParameters(
                    id = 1,
                    uri = "urn:ietf:params:rtp-hdrext:sdes:mid",
                    encrypt = false,
                    parameters = emptyMap<String, Any>()
                ),
                RtpHeaderExtensionParameters(
                    id = 4,
                    uri = "http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time",
                    encrypt = false,
                    parameters = emptyMap<String, Any>()
                ),
                RtpHeaderExtensionParameters(
                    id = 10,
                    uri = "urn:ietf:params:rtp-hdrext:ssrc-audio-level",
                    encrypt = false,
                    parameters = emptyMap<String, Any>()
                )
            ),
            encodings = listOf(
                RtpEncodingParameters(
                    ssrc = 980715670
                )
            ),
            rtcp = RtcpParameters(
                cname = "gJYTQGm9n8z+u1ql",
                reducedSize = true,
                mux = true
            )
        ))
    }

    @Test
    fun serializeRtpCodecCapability() {
        val adapter = moshi.adapter(RtpCodecCapability::class.java)
        assertThat(adapter.toJson(RtpCodecCapability(
            kind = "audio",
            mimeType = "audio/opus",
            clockRate = 48000,
            channels = 2,
            parameters = mapOf(
                "minptime" to 10,
                "useinbandfec" to 1,
                "sprop-stereo" to 1,
                "usedtx" to 1
            ),
            rtcpFeedback = emptyList(),
            preferredPayloadType = 0
        ))).isEqualTo("""
            {"kind":"audio","mimeType":"audio/opus","preferredPayloadType":0,"clockRate":48000,"channels":2,"parameters":{"minptime":10,"useinbandfec":1,"sprop-stereo":1,"usedtx":1},"rtcpFeedback":[]}
        """.trimIndent())
    }

    @Test
    fun deserializeRtpCodecCapability() {
        val adapter = moshi.adapter(RtpCodecCapability::class.java)
        assertThat(adapter.fromJson("""
            {"kind":"audio","mimeType":"audio/opus","preferredPayloadType":0,"clockRate":48000,"channels":2,"parameters":{"minptime":10,"useinbandfec":1,"sprop-stereo":1,"usedtx":1},"rtcpFeedback":[]}
        """.trimIndent())).isEqualTo(RtpCodecCapability(
            kind = "audio",
            mimeType = "audio/opus",
            clockRate = 48000,
            channels = 2,
            parameters = mapOf(
                "minptime" to 10.0,
                "useinbandfec" to 1.0,
                "sprop-stereo" to 1.0,
                "usedtx" to 1.0
            ),
            rtcpFeedback = emptyList(),
            preferredPayloadType = 0
        ))
    }
}