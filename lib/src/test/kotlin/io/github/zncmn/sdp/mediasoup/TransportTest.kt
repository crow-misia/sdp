package io.github.zncmn.sdp.mediasoup

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

import com.squareup.moshi.Moshi

internal class TransportTest {
    private val moshi = Moshi.Builder().build()

    // "iceParameters":{"iceLite":true,"password":"passwordXXXXXX","usernameFragment":"usernameXXXXXX"}
    // "iceCandidates":[{"foundation":"udpcandidate","ip":"123.45.67.8","port":12345,"priority":1076302079,"protocol":"udp","type":"host"}]
    // "dtlsParameters":{"fingerprints":[{"algorithm":"sha-1","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:13"},{"algorithm":"sha-224","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:14"},{"algorithm":"sha-256","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:15"},{"algorithm":"sha-384","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:16"},{"algorithm":"sha-512","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:17"}],"role":"auto"}

    @Test
    fun serializeIceParameters() {
        val adapter = moshi.adapter(IceParameters::class.java)
        assertThat(adapter.toJson(IceParameters(
            iceLite = true,
            password = "passwordXXXXXX",
            usernameFragment = "usernameXXXXXX"
        ))).isEqualTo("""
            {"usernameFragment":"usernameXXXXXX","password":"passwordXXXXXX","iceLite":true}
        """.trimIndent())
    }

    @Test
    fun deserializeIceParameters() {
        val adapter = moshi.adapter(IceParameters::class.java)
        assertThat(adapter.fromJson("""
            {"iceLite":true,"password":"passwordXXXXXX","usernameFragment":"usernameXXXXXX"}
        """.trimIndent())).isEqualTo(IceParameters(
            iceLite = true,
            password = "passwordXXXXXX",
            usernameFragment = "usernameXXXXXX"
        ))
    }

    @Test
    fun serializeIceCandidate() {
        val adapter = moshi.adapter(IceCandidate::class.java)
        assertThat(adapter.toJson(IceCandidate(
            foundation = "udpcandidate",
            ip = "123.45.67.8",
            port = 12345,
            priority = 1076302079,
            protocol = "udp",
            type = "host"
        ))).isEqualTo("""
            {"foundation":"udpcandidate","priority":1076302079,"ip":"123.45.67.8","protocol":"udp","port":12345,"type":"host"}
        """.trimIndent())
    }

    @Test
    fun deserializeIceCandidate() {
        val adapter = moshi.adapter(IceCandidate::class.java)
        assertThat(adapter.fromJson("""
            {"foundation":"udpcandidate","ip":"123.45.67.8","port":12345,"priority":1076302079,"protocol":"udp","type":"host"}
        """.trimIndent())).isEqualTo(IceCandidate(
            foundation = "udpcandidate",
            ip = "123.45.67.8",
            port = 12345,
            priority = 1076302079,
            protocol = "udp",
            type = "host"
        ))
    }

    @Test
    fun serializeDtlsParameters() {
        val adapter = moshi.adapter(DtlsParameters::class.java)
        assertThat(adapter.toJson(DtlsParameters(
            fingerprints = listOf(
                DtlsFingerprint("sha-1", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:13"),
                DtlsFingerprint("sha-224", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:14"),
                DtlsFingerprint("sha-256", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:15"),
                DtlsFingerprint("sha-384", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:16"),
                DtlsFingerprint("sha-512", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:17")
            ),
            role = DtlsRole.AUTO
        ))).isEqualTo("""
            {"role":"auto","fingerprints":[{"algorithm":"sha-1","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:13"},{"algorithm":"sha-224","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:14"},{"algorithm":"sha-256","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:15"},{"algorithm":"sha-384","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:16"},{"algorithm":"sha-512","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:17"}]}
        """.trimIndent())
    }

    @Test
    fun deserializeDtlsParameters() {
        val adapter = moshi.adapter(DtlsParameters::class.java)
        assertThat(adapter.fromJson("""
            {"fingerprints":[{"algorithm":"sha-1","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:13"},{"algorithm":"sha-224","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:14"},{"algorithm":"sha-256","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:15"},{"algorithm":"sha-384","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:16"},{"algorithm":"sha-512","value":"00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:17"}],"role":"auto"}
        """.trimIndent())).isEqualTo(DtlsParameters(
            fingerprints = listOf(
                DtlsFingerprint("sha-1", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:13"),
                DtlsFingerprint("sha-224", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:14"),
                DtlsFingerprint("sha-256", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:15"),
                DtlsFingerprint("sha-384", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:16"),
                DtlsFingerprint("sha-512", "00:01:02:03:04:05:06:07:08:09:0A:0B:0C:0D:0E:0F:10:11:12:17")
            ),
            role = DtlsRole.AUTO
        ))
    }
}