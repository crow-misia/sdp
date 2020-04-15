package io.github.zncmn.sdp

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test


internal class SessionDescriptionTest {

    @Test
    fun parse() {
        val actual = SessionDescription.parse("""
      v=0
      o=jdoe 2890844526 2890842807 IN IP4 10.47.16.5
      s=SDP Seminar
      i=A Seminar on the session description protocol
      u=http://www.example.com/seminars/sdp.pdf
      e=j.doe@example.com (Jane Doe)
      c=IN IP4 224.2.17.12/127
      t=2873397496 2873404696
      a=recvonly
      m=audio 49170 RTP/AVP 0
      m=video 51372 RTP/AVP 99
      a=rtpmap:99 h263-1998/90000
        """.trimIndent())
        assertThat(actual.toString()).isEqualTo(SessionDescription(
            version = SdpVersion(),
            origin = SdpOrigin("jdoe", "2890844526", 2890842807, "IN", "IP4", "10.47.16.5"),
            sessionName = SdpSessionName("SDP Seminar"),
            information = SdpSessionInformation("A Seminar on the session description protocol"),
            uris = listOf(SdpUri("http://www.example.com/seminars/sdp.pdf")),
            emails = listOf(SdpEmail("j.doe@example.com (Jane Doe)")),
            connection = SdpConnection("IN", "IP4", "224.2.17.12/127"),
            timings = listOf(SdpTiming(2873397496L, 2873404696L)),
            attributes = listOf(BaseSdpAttribute("recvonly")),
            mediaDescriptions = listOf(
                MediaDescription("audio", 49170, null, listOf("RTP", "AVP"), listOf("0")),
                MediaDescription("video", 51372, null, listOf("RTP", "AVP"), listOf("99"),
                    attributes = listOf(
                        RTPMapAttribute(99, "h263-1998", 90000)
                    ))
            )
        ).toString())
    }


    @Test
    fun parseIpv4() {
        val expected = """
v=0
o=- 3163544789 1 IN IP4 127.0.0.1
s=webrtc (chrome 22.0.1189.0) - Doubango Telecom (sipML5 r000)
t=0 0
m=audio 62359 RTP/SAVPF 103 104 0 8 106 105 13 126
c=IN IP4 80.39.43.34
a=rtpmap:105 CN/16000
a=rtcp:62359 IN IP4 80.39.43.34
a=candidate:4242042849 1 udp 2130714367 192.168.153.1 54521 typ host generation 0
a=candidate:4242042849 2 udp 2130714367 192.168.153.1 54521 typ host generation 0
a=candidate:3028854479 1 udp 2130714367 192.168.1.44 54522 typ host generation 0
a=candidate:3028854479 2 udp 2130714367 192.168.1.44 54522 typ host generation 0
a=candidate:3471623853 1 udp 2130714367 192.168.64.1 54523 typ host generation 0
a=candidate:3471623853 2 udp 2130714367 192.168.64.1 54523 typ host generation 0
a=candidate:1117314843 1 udp 1912610559 80.39.43.34 62359 typ srflx generation 0
a=candidate:1117314843 2 udp 1912610559 80.39.43.34 62359 typ srflx generation 0
a=candidate:2992345873 1 tcp 1694506751 192.168.153.1 54185 typ host generation 0
a=candidate:2992345873 2 tcp 1694506751 192.168.153.1 54185 typ host generation 0
a=candidate:4195047999 1 tcp 1694506751 192.168.1.44 54186 typ host generation 0
a=candidate:4195047999 2 tcp 1694506751 192.168.1.44 54186 typ host generation 0
a=candidate:2154773085 1 tcp 1694506751 192.168.64.1 54187 typ host generation 0
a=candidate:2154773085 2 tcp 1694506751 192.168.64.1 54187 typ host generation 0
a=ice-ufrag:2HMP+QEvuPxeZo3I
a=ice-pwd:RjRF5Wtaj7XMk5skkrJ6TP6k
a=sendrecv
a=mid:audio
a=rtcp-mux
a=crypto:1 AES_CM_128_HMAC_SHA1_80 inline:CoKH4lo5t34SYU0pqeJGwes2gJCEWKFmLUv/q0sN
a=rtpmap:103 ISAC/16000
a=rtpmap:104 ISAC/32000
a=rtpmap:0 PCMU/8000
a=rtpmap:8 PCMA/8000
a=rtpmap:106 CN/32000
a=rtpmap:13 CN/8000
a=rtpmap:126 telephone-event/8000
a=ssrc:4221941097 cname:61TRWZq6iadCKCYD
a=ssrc:4221941097 mslabel:HnAeVefwdG64baIr9EdbXNwEChe67aSRJFcW
a=ssrc:4221941097 label:HnAeVefwdG64baIr9EdbXNwEChe67aSRJFcW00
m=video 62359 RTP/SAVPF 100 101 102
c=IN IP4 80.39.43.34
a=rtcp:62359 IN IP4 80.39.43.34
a=candidate:4242042849 1 udp 2130714367 192.168.153.1 54521 typ host generation 0
a=candidate:4242042849 2 udp 2130714367 192.168.153.1 54521 typ host generation 0
a=candidate:3028854479 1 udp 2130714367 192.168.1.44 54522 typ host generation 0
a=candidate:3028854479 2 udp 2130714367 192.168.1.44 54522 typ host generation 0
a=candidate:3471623853 1 udp 2130714367 192.168.64.1 54523 typ host generation 0
a=candidate:3471623853 2 udp 2130714367 192.168.64.1 54523 typ host generation 0
a=candidate:1117314843 1 udp 1912610559 80.39.43.34 62359 typ srflx generation 0
a=candidate:1117314843 2 udp 1912610559 80.39.43.34 62359 typ srflx generation 0
a=candidate:2992345873 1 tcp 1694506751 192.168.153.1 54185 typ host generation 0
a=candidate:2992345873 2 tcp 1694506751 192.168.153.1 54185 typ host generation 0
a=candidate:4195047999 1 tcp 1694506751 192.168.1.44 54186 typ host generation 0
a=candidate:4195047999 2 tcp 1694506751 192.168.1.44 54186 typ host generation 0
a=candidate:2154773085 1 tcp 1694506751 192.168.64.1 54187 typ host generation 0
a=candidate:2154773085 2 tcp 1694506751 192.168.64.1 54187 typ host generation 0
a=ice-ufrag:2HMP+QEvuPxeZo3I
a=ice-pwd:RjRF5Wtaj7XMk5skkrJ6TP6k
a=sendrecv
a=mid:video
a=rtcp-mux
a=crypto:1 AES_CM_128_HMAC_SHA1_80 inline:CoKH4lo5t34SYU0pqeJGwes2gJCEWKFmLUv/q0sN
a=rtpmap:100 VP8/90000
a=rtpmap:101 red/90000
a=rtpmap:102 ulpfec/90000
a=ssrc:2432199953 cname:61TRWZq6iadCKCYD
a=ssrc:2432199953 mslabel:HnAeVefwdG64baIr9EdbXNwEChe67aSRJFcW
a=ssrc:2432199953 label:HnAeVefwdG64baIr9EdbXNwEChe67aSRJFcW10

        """.trimIndent().lines().joinToString("\r\n")

        val actual = SessionDescription.parse(expected)
        println(actual)
        assertThat(actual.toString()).isEqualTo(expected)
    }

    @Test
    fun parseIpv6() {
        val expected = """
v=0
o=- 5418966674060870245 2 IN IP4 127.0.0.1
s=-
t=0 0
a=msid-semantic: WMS O00y05k6HkwvEgtb8eAei31E3yithVnmEtb6
m=audio 55906 RTP/SAVPF 98 9 0 8
c=IN IP6 2001::5ef5:79fd:1cdf:b0f:b981:52c7
a=rtcp:9 IN IP4 0.0.0.0
a=candidate:2377980607 1 udp 2122255103 2001::5ef5:79fd:1cdf:b0f:b981:52c7 55906 typ host generation 0
a=candidate:1310573339 1 udp 2122194687 172.16.0.13 55907 typ host generation 0
a=ice-ufrag:iPXReusedakIlQVk
a=ice-pwd:qfxtJvHZbAGy4xEmYppj5leH
a=fingerprint:sha-256 CB:C7:0E:FB:D3:B3:76:40:D4:16:4B:11:89:73:0C:D4:08:25:97:12:71:67:13:17:59:AA:CB:C3:45:11:AE:89
a=setup:active
a=mid:audio
a=extmap:1 urn:ietf:params:rtp-hdrext:ssrc-audio-level
a=sendrecv
a=rtcp-mux
a=rtpmap:98 opus/48000/2
a=fmtp:98 minptime=10; useinbandfec=1
a=rtpmap:9 G722/8000
a=rtpmap:0 PCMU/8000
a=rtpmap:8 PCMA/8000
a=maxptime:60
a=ssrc:3838492348 cname:GU1rL0d7X65rYv5b
a=ssrc:3838492348 msid:O00y05k6HkwvEgtb8eAei31E3yithVnmEtb6 9c2063d7-e1bc-42f1-9226-eabc8bf6d9fb
a=ssrc:3838492348 mslabel:O00y05k6HkwvEgtb8eAei31E3yithVnmEtb6
a=ssrc:3838492348 label:9c2063d7-e1bc-42f1-9226-eabc8bf6d9fb
m=video 55908 RTP/SAVPF 107 110 108
c=IN IP6 2001::5ef5:79fd:1cdf:b0f:b981:52c7
a=rtcp:9 IN IP4 0.0.0.0
a=candidate:2377980607 1 udp 2122255103 2001::5ef5:79fd:1cdf:b0f:b981:52c7 55908 typ host generation 0
a=candidate:1310573339 1 udp 2122194687 172.16.0.13 55909 typ host generation 0
a=ice-ufrag:hIETTuIQYOLIV2TL
a=ice-pwd:l9j44z+5IyoSJyca6zLNX0ZI
a=fingerprint:sha-256 CB:C7:0E:FB:D3:B3:76:40:D4:16:4B:11:89:73:0C:D4:08:25:97:12:71:67:13:17:59:AA:CB:C3:45:11:AE:89
a=setup:active
a=mid:video
a=extmap:2 urn:ietf:params:rtp-hdrext:toffset
a=extmap:3 http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time
a=sendrecv
a=rtcp-mux
a=rtpmap:107 VP8/90000
a=rtcp-fb:107 ccm fir
a=rtcp-fb:107 nack pli
a=rtcp-fb:107 goog-remb
a=rtpmap:110 rtx/90000
a=fmtp:110 apt=107
a=rtpmap:108 ulpfec/90000
a=ssrc-group:FID 1887964958 2814730704
a=ssrc:1887964958 cname:GU1rL0d7X65rYv5b
a=ssrc:1887964958 msid:O00y05k6HkwvEgtb8eAei31E3yithVnmEtb6 77150781-678e-4cd3-a56d-233c01cd4ffd
a=ssrc:1887964958 mslabel:O00y05k6HkwvEgtb8eAei31E3yithVnmEtb6
a=ssrc:1887964958 label:77150781-678e-4cd3-a56d-233c01cd4ffd
a=ssrc:2814730704 cname:GU1rL0d7X65rYv5b
a=ssrc:2814730704 msid:O00y05k6HkwvEgtb8eAei31E3yithVnmEtb6 77150781-678e-4cd3-a56d-233c01cd4ffd
a=ssrc:2814730704 mslabel:O00y05k6HkwvEgtb8eAei31E3yithVnmEtb6
a=ssrc:2814730704 label:77150781-678e-4cd3-a56d-233c01cd4ffd

        """.trimIndent().lines().joinToString("\r\n")

        val actual = SessionDescription.parse(expected)
        println(actual)
        assertThat(actual.toString()).isEqualTo(expected)
    }
}