package io.github.zncmn.sdp.mediasoup

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.squareup.moshi.Moshi
import io.github.zncmn.sdp.webrtc.RTCPriorityType
import org.junit.jupiter.api.Test

internal class SctpParametersTest {
    private val moshi = Moshi.Builder().build()

    @Test
    fun serializeNumSctpStreams() {
        val adapter = moshi.adapter(NumSctpStreams::class.java)
        assertThat(adapter.toJson(NumSctpStreams(1, 2))).isEqualTo("""
            {"OS":1,"MIS":2}
        """.trimIndent())
    }

    @Test
    fun deserializeNumSctpStreams() {
        val adapter = moshi.adapter(NumSctpStreams::class.java)
        assertThat(adapter.fromJson("""
            {"OS":1, "MIS":2}
        """.trimIndent())).isEqualTo(NumSctpStreams(1, 2))
    }

    @Test
    fun serializeScpParameters() {
        val adapter = moshi.adapter(SctpParameters::class.java)
        assertThat(adapter.toJson(SctpParameters(5000,1, 2, 3))).isEqualTo("""
            {"port":5000,"OS":1,"MIS":2,"maxMessageSize":3}
        """.trimIndent())
    }

    @Test
    fun deserializeScpParameters() {
        val adapter = moshi.adapter(SctpParameters::class.java)
        assertThat(adapter.fromJson("""
            {"port":5000,"OS":1, "MIS":2, "maxMessageSize":3}
        """.trimIndent())).isEqualTo(SctpParameters(5000, 1, 2, 3))
    }


    @Test
    fun serializeSctpStreamParameters() {
        val adapter = moshi.adapter(SctpStreamParameters::class.java)
        assertThat(adapter.toJson(SctpStreamParameters(1,true, 2, 3, RTCPriorityType.HIGH, "label", "udp"))).isEqualTo("""
            {"streamId":1,"ordered":true,"maxPacketLifeTime":2,"maxRetransmits":3,"priority":"high","label":"label","protocol":"udp"}
        """.trimIndent())
    }

    @Test
    fun deserializeSctpStreamParameters() {
        val adapter = moshi.adapter(SctpStreamParameters::class.java)
        assertThat(adapter.fromJson("""
            {"streamId":1,"ordered":true,"maxPacketLifeTime":2,"maxRetransmits":3,"priority":"low","label":"label","protocol":"udp"}
         """.trimIndent())).isEqualTo(SctpStreamParameters(1, true, 2, 3, RTCPriorityType.LOW, "label", "udp"))
    }
}