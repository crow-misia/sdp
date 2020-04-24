package io.github.zncmn.sdp

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.zncmn.sdp.attribute.*
import org.junit.jupiter.api.Test

internal class SessionDescriptionTest {

    @Test
    fun parse() {
        val actual = SdpSessionDescription.parse("""
v=0
o=jdoe 2890844526 2890842807 IN IP4 10.47.16.5
s=SDP Seminar
i=A Seminar on the session description protocol
u=http://www.example.com/seminars/sdp.pdf
e=j.doe@example.com (Jane Doe)
p=+81012345678
c=IN IP4 224.2.17.12/127
t=2873397496 2873404696
b=AS:4000
a=sendrecv
m=audio 49170 RTP/AVP 0
a=recvonly
a=rtcp:65179 IN IP4 123.45.67.89
a=rtcp:12345
a=control:streamId=0
a=rtcp-fb:98 trr-int 100
a=rtcp-fb:98 nack rpsi
a=extmap:2 urn:ietf:params:rtp-hdrext:toffset
a=extmap:1/recvonly URI-gps-string
a=extmap:3 urn:ietf:params:rtp-hdrext:encrypt urn:ietf:params:rtp-hdrext:smpte-tc 25@600/24
a=fmtp:98 minptime=10;useinbandfec=1
m=video 51372 RTP/AVP 99
a=sendonly
a=rtpmap:99 h263-1998/90000
a=rtpmap:99 h263-1998
a=framerate:29.97
a=rid:0 send max-width=1280; max-height=720; max-fps=30
a=rid:1 send max-width=640; max-height=360; max-fps=15
a=rid:2 send max-width=320; max-height=180; max-fps=15
a=simulcast: send rid=0;1;2
m=video 51372 RTP/AVP 98
a=simulcast:send hi,mid,low
a=rid:hi send
a=rid:mid send
a=rid:low send
a=ssrc-group:FID 123 456 789
a=ssrc:123 cname:foo
a=ssrc:456 cname:foo
a=ssrc:789 cname:foo
a=extmap-allow-mixed
a=crypto:1 AES_CM_128_HMAC_SHA1_80 inline:PS1uQCVeeCFCanVmcjkpPywjNWhcYD0mXXtxaVBR|2^20|1:32
a=setup:actpass
a=mid:  1
a=msid:0c8b064d-d807-43b4-b434-f92a889d8587 98178685-d409-46e0-8e16-7ef0db0db64a
a=ptime:20
a=maxptime:60
a=ice-lite
a=ice-ufrag:  F7gI
a=ice-pwd:  x9cml/YzichV2+XlhiMu8g
a=fingerprint:SHA-1 00:11:22:33:44:55:66:77:88:99:AA:BB:CC:DD:EE:FF:00:11:22:33
a=candidate:0 1 UDP 2113667327 203.0.113.1 54400 typ host
a=candidate:1162875081 1 udp 2113937151 192.168.34.75 60017 typ host generation 0 network-id 3 network-cost 10
a=candidate:3289912957 2 udp 1845501695 193.84.77.194 60017 typ srflx raddr 192.168.34.75 rport 60017 generation 0 network-id 3 network-cost 10
a=candidate:229815620 1 tcp 1518280447 192.168.150.19 60017 typ host tcptype active generation 0 network-id 3 network-cost 10
a=candidate:3289912957 2 tcp 1845501695 193.84.77.194 60017 typ srflx raddr 192.168.34.75 rport 60017 tcptype passive generation 0 network-id 3 network-cost 10
a=end-of-candidates
a=remote-candidates:1 203.0.113.1 54400 2 203.0.113.1 54401
a=ice-options:google-ice
a=ssrc:2566107569 cname:t9YU8M1UxTF8Y1A1
a=ssrc:2566107569 cname
a=ssrc-group:FEC 1 2
a=ssrc-group:FEC-FR 3004364195 1080772241
a=msid-semantic: WMS Jvlam5X3SX1OP6pn20zWogvaKJz5Hjf9OnlV
a=msid-semantic: WMS
a=group:BUNDLE audio video
a=rtcp-mux
a=rtcp-rsize
a=sctpmap:5000 webrtc-datachannel 1024
a=x-google-flag:conference
a=rid:1 send max-width=1280;max-height=720;max-fps=30;depend=0
a=imageattr:97 send [x=800,y=640,sar=1.1,q=0.6] [x=480,y=320] recv [x=330,y=250]
a=imageattr:* send [x=800,y=640] recv *
a=imageattr:100 recv [x=320,y=240]
a=simulcast:send 1,2,3;~4,~5 recv 6;~7,~8
a=simulcast:recv 1;4,5 send 6;7
a=simulcast: recv pt=97;98 send pt=97
a=simulcast: send rid=5;6;7 paused=6,7
a=framerate:25
a=framerate:29.97
a=source-filter: incl IN IP4 239.5.2.31 10.1.15.5
a=ts-refclk:ptp=IEEE1588-2008:00-50-C2-FF-FE-90-04-37:0
a=mediaclk:direct=0
        """.trimIndent())
        val expected = SdpSessionDescription.of(
            version = SdpVersion.of(),
            origin = SdpOrigin.of("jdoe", 2890844526, 2890842807, "IN", "IP4", "10.47.16.5"),
            sessionName = SdpSessionName.of("SDP Seminar"),
            information = SdpSessionInformation.of("A Seminar on the session description protocol"),
            uris = listOf(SdpUri.of("http://www.example.com/seminars/sdp.pdf")),
            emails = listOf(SdpEmail.of("j.doe@example.com (Jane Doe)")),
            phones = listOf(SdpPhone.of("+81012345678")),
            connection = SdpConnection.of("IN", "IP4", "224.2.17.12/127"),
            bandwidths = listOf(SdpBandwidth("AS", 4000)),
            timings = listOf(SdpTiming.of(2873397496L, 2873404696L)),
            attributes = listOf(SendRecvAttribute),
            mediaDescriptions = listOf(
                SdpMediaDescription.of("audio", 49170, null, listOf("RTP", "AVP"), listOf("0"),
                    attributes = listOf(
                        RecvOnlyAttribute,
                        RTCPAttribute.of(65179, "IN", "IP4", "123.45.67.89"),
                        RTCPAttribute.of(12345),
                        ControlAttribute.of("streamId=0"),
                        RTCPFbAttribute.of("98", "trr-int", "100"),
                        RTCPFbAttribute.of("98", "nack", "rpsi"),
                        ExtMapAttribute.of(2, uri = "urn:ietf:params:rtp-hdrext:toffset"),
                        ExtMapAttribute.of(1, Direction.RECVONLY, uri = "URI-gps-string"),
                        ExtMapAttribute.of(3, null, "urn:ietf:params:rtp-hdrext:encrypt", "urn:ietf:params:rtp-hdrext:smpte-tc", "25@600/24"),
                        FormatAttribute.of(98).also {
                            it.addParameter("minptime", "10")
                            it.addParameter("useinbandfec", "1")
                        }
                    )),
                SdpMediaDescription.of("video", 51372, null, listOf("RTP", "AVP"), listOf("99"),
                    attributes = listOf(
                        SendOnlyAttribute,
                        RTPMapAttribute.of(99, "h263-1998", 90000),
                        RTPMapAttribute.of(99, "h263-1998"),
                        FramerateAttribute.of(29.97),
                        RidAttribute.of("0", "send", "max-width=1280; max-height=720; max-fps=30"),
                        RidAttribute.of("1", "send", "max-width=640; max-height=360; max-fps=15"),
                        RidAttribute.of("2", "send", "max-width=320; max-height=180; max-fps=15"),
                        Simulcast03Attribute.of("send rid=0;1;2")
                    )),
                SdpMediaDescription.of("video", 51372, null, listOf("RTP", "AVP"), listOf("98"),
                    attributes = listOf(
                        SimulcastAttribute.of("send", "hi,mid,low"),
                        RidAttribute.of("hi", "send"),
                        RidAttribute.of("mid", "send"),
                        RidAttribute.of("low", "send"),
                        SsrcGroupAttribute.of("FID", 123, 456, 789),
                        SsrcAttribute.of(123, "cname", "foo"),
                        SsrcAttribute.of(456, "cname", "foo"),
                        SsrcAttribute.of(789, "cname", "foo"),
                        ExtmapAllowMixedAttribute.of(),
                        CryptoAttribute.of(1L, "AES_CM_128_HMAC_SHA1_80", "inline:PS1uQCVeeCFCanVmcjkpPywjNWhcYD0mXXtxaVBR|2^20|1:32"),
                        SetupAttribute.of(SetupAttribute.Type.ACTPASS),
                        MidAttribute.of("1"),
                        MsidAttribute.of("0c8b064d-d807-43b4-b434-f92a889d8587", "98178685-d409-46e0-8e16-7ef0db0db64a"),
                        PtimeAttribute.of(20),
                        MaxPtimeAttribute.of(60),
                        IceLiteAttribute,
                        IceUfragAttribute.of("F7gI"),
                        IcePwdAttribute.of("x9cml/YzichV2+XlhiMu8g"),
                        FingerprintAttribute.of("SHA-1", "00:11:22:33:44:55:66:77:88:99:AA:BB:CC:DD:EE:FF:00:11:22:33"),
                        CandidateAttribute.of("0", 1, "UDP", 2113667327, "203.0.113.1", 54400, "host"),
                        CandidateAttribute.of("1162875081", 1, "udp", 2113937151, "192.168.34.75", 60017, "host", mapOf("generation" to "0", "network-id" to "3", "network-cost" to "10")),
                        CandidateAttribute.of("3289912957", 2, "udp", 1845501695, "193.84.77.194", 60017, "srflx", mapOf("raddr" to "192.168.34.75", "rport" to "60017", "generation" to "0", "network-id" to "3", "network-cost" to "10")),
                        CandidateAttribute.of("229815620", 1, "tcp", 1518280447, "192.168.150.19", 60017, "host", mapOf("tcptype" to "active", "generation" to "0", "network-id" to "3", "network-cost" to "10")),
                        CandidateAttribute.of("3289912957", 2, "tcp", 1845501695, "193.84.77.194", 60017, "srflx", mapOf("raddr" to "192.168.34.75", "rport" to "60017", "tcptype" to "passive", "generation" to "0", "network-id" to "3", "network-cost" to "10")),
                        EndOfCandidatesAttribute,
                        RemoteCandidateAttribute.of("1 203.0.113.1 54400 2 203.0.113.1 54401"),
                        IceOptionsAttribute.of("google-ice"),
                        SsrcAttribute.of(2566107569L, "cname", "t9YU8M1UxTF8Y1A1"),
                        SsrcAttribute.of(2566107569L, "cname"),
                        SsrcGroupAttribute.of("FEC", 1, 2),
                        SsrcGroupAttribute.of("FEC-FR",  3004364195L, 1080772241L),
                        MsidSemanticAttribute.of("WMS", "Jvlam5X3SX1OP6pn20zWogvaKJz5Hjf9OnlV"),
                        MsidSemanticAttribute.of("WMS"),
                        GroupAttribute.of("BUNDLE", "audio", "video"),
                        RTCPMuxAttribute.of(),
                        RTCPRsizeAttribute.of(),
                        SctpMapAttribute.of(5000, "webrtc-datachannel", 1024),
                        XgoogleFlagAttribute.of("conference"),
                        RidAttribute.of("1", "send", "max-width=1280;max-height=720;max-fps=30;depend=0"),
                        ImageAttrsAttribute.of("97", "send", "[x=800,y=640,sar=1.1,q=0.6] [x=480,y=320]", "recv", "[x=330,y=250]"),
                        ImageAttrsAttribute.of("*", "send", "[x=800,y=640]", "recv", "*"),
                        ImageAttrsAttribute.of("100", "recv", "[x=320,y=240]"),
                        SimulcastAttribute.of("send", "1,2,3;~4,~5", "recv", "6;~7,~8"),
                        SimulcastAttribute.of("recv", "1;4,5", "send", "6;7"),
                        Simulcast03Attribute.of("recv pt=97;98 send pt=97"),
                        Simulcast03Attribute.of("send rid=5;6;7 paused=6,7"),
                        FramerateAttribute.of(25),
                        FramerateAttribute.of(29.97),
                        SourceFilterAttribute.of("incl", "IN", "IP4", "239.5.2.31", "10.1.15.5"),
                        TsRefclkAttribute.of("ptp=IEEE1588-2008:00-50-C2-FF-FE-90-04-37:0"),
                        MediaclkAttribute.of("direct=0")
                    ))
            )
        )

        /**
        a=source-filter: incl IN IP4 239.5.2.31 10.1.15.5
         */
        assertThat(actual).isEqualTo(expected)
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
b=AS:1000
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

        val actual = SdpSessionDescription.parse(expected)
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
a=fmtp:98 minptime=10;useinbandfec=1
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

        val actual = SdpSessionDescription.parse(expected)
        println(actual)
        assertThat(actual.toString()).isEqualTo(expected)
    }
}