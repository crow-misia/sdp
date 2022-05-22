package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.*
import io.kotest.assertions.asClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File
import java.math.BigInteger

class SessionDescriptionTest : StringSpec({
    "パーステスト" {
        val expectedStr = File("src/test/data/parse_test.sdp").readText()
        val actual = SdpSessionDescription.parse(expectedStr)

        val testMediaDescription = SdpMediaDescription.of(
            "audio", 49170, null, listOf("RTP", "AVP"), listOf("0"),
            attributes = listOf(
                MidAttribute.of("123"),
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
                    it.addParameter("minptime", 10)
                    it.addParameter("useinbandfec", 1)
                }
            ))

        val expected = SdpSessionDescription.of(
            version = SdpVersion.of(),
            origin = SdpOrigin.of(
                "jdoe",
                BigInteger("2890844526"),
                BigInteger("2890842807"),
                "IN",
                "IP4",
                "10.47.16.5"
            ),
            sessionName = SdpSessionName.of("SDP Seminar"),
            information = SdpSessionInformation.of("A Seminar on the session description protocol"),
            uris = listOf(SdpUri.of("http://www.example.com/seminars/sdp.pdf")),
            emails = listOf(SdpEmail.of("j.doe@example.com (Jane Doe)")),
            phones = listOf(SdpPhone.of("+81012345678")),
            connection = SdpConnection.of("IN", "IP4", "224.2.17.12", 127),
            bandwidths = listOf(SdpBandwidth("AS", 4000)),
            timings = listOf(
                SdpTiming.of(
                    2873397496L,
                    2873404696L,
                    repeatTime = SdpRepeatTime.of("604800", "3600", "0", "90000")
                )
            ),
            timeZones = SdpTimeZones.of(SdpTimeZone.of(2882844526L, "-1h"), SdpTimeZone.of(2898848070L, "0")),
            key = EncryptionKey.of(EncryptionKey.Method.BASE64, "<encoded encryption key>"),
            attributes = listOf(SendRecvAttribute),
            mediaDescriptions = listOf(
                testMediaDescription,
                SdpMediaDescription.of(
                    "video", 51372, null, listOf("RTP", "AVP"), listOf("99"),
                    attributes = listOf(
                        SendOnlyAttribute,
                        CNameAttribute.of("abc"),
                        RTPMapAttribute.of(99, "h263-1998", 90000),
                        RTPMapAttribute.of(99, "h263-1998"),
                        FramerateAttribute.of(29.97),
                        RidAttribute.of("0", StreamDirection.SEND, "max-width=1280; max-height=720; max-fps=30"),
                        RidAttribute.of("1", StreamDirection.SEND, "max-width=640; max-height=360; max-fps=15"),
                        RidAttribute.of("2", StreamDirection.SEND, "max-width=320; max-height=180; max-fps=15"),
                        Simulcast03Attribute.of("send rid=0;1;2")
                    )
                ),
                SdpMediaDescription.of(
                    "video", 51372, null, listOf("RTP", "AVP"), listOf("98"),
                    information = SdpSessionInformation.of("media title"),
                    key = EncryptionKey.of(EncryptionKey.Method.PROMPT),
                    connections = listOf(SdpConnection.of("IN", "IP4", "224.2.17.12", 127)),
                    bandwidths = listOf(SdpBandwidth.of("AS", 500)),
                    attributes = listOf(
                        SimulcastAttribute.of(StreamDirection.SEND, "hi,mid,low"),
                        RidAttribute.of("hi", StreamDirection.SEND),
                        RidAttribute.of("mid", StreamDirection.SEND),
                        RidAttribute.of("low", StreamDirection.SEND),
                        SsrcGroupAttribute.of("FID", 123, 456, 789),
                        SsrcAttribute.of(123, "cname", "foo"),
                        SsrcAttribute.of(456, "cname", "foo"),
                        SsrcAttribute.of(789, "cname", "foo"),
                        ExtmapAllowMixedAttribute.of(),
                        CryptoAttribute.of(
                            1L,
                            "AES_CM_128_HMAC_SHA1_80",
                            "inline:PS1uQCVeeCFCanVmcjkpPywjNWhcYD0mXXtxaVBR|2^20|1:32"
                        ),
                        SetupAttribute.of(SetupAttribute.Type.ACTPASS),
                        MidAttribute.of("1"),
                        MsidAttribute.of(
                            "0c8b064d-d807-43b4-b434-f92a889d8587",
                            "98178685-d409-46e0-8e16-7ef0db0db64a"
                        ),
                        PtimeAttribute.of(20),
                        MaxPtimeAttribute.of(60),
                        IceLiteAttribute,
                        IceUfragAttribute.of("F7gI"),
                        IcePwdAttribute.of("x9cml/YzichV2+XlhiMu8g"),
                        FingerprintAttribute.of("SHA-1", "00:11:22:33:44:55:66:77:88:99:AA:BB:CC:DD:EE:FF:00:11:22:33"),
                        CandidateAttribute.of("0", 1, "UDP", 2113667327, "203.0.113.1", 54400, "host"),
                        CandidateAttribute.of(
                            "1162875081",
                            1,
                            "udp",
                            2113937151,
                            "192.168.34.75",
                            60017,
                            "host",
                            mapOf("generation" to "0", "network-id" to "3", "network-cost" to "10")
                        ),
                        CandidateAttribute.of(
                            "3289912957",
                            2,
                            "udp",
                            1845501695,
                            "193.84.77.194",
                            60017,
                            "srflx",
                            mapOf(
                                "raddr" to "192.168.34.75",
                                "rport" to "60017",
                                "generation" to "0",
                                "network-id" to "3",
                                "network-cost" to "10"
                            )
                        ),
                        CandidateAttribute.of(
                            "229815620",
                            1,
                            "tcp",
                            1518280447,
                            "192.168.150.19",
                            60017,
                            "host",
                            mapOf(
                                "tcptype" to "active",
                                "generation" to "0",
                                "network-id" to "3",
                                "network-cost" to "10"
                            )
                        ),
                        CandidateAttribute.of(
                            "3289912957",
                            2,
                            "tcp",
                            1845501695,
                            "193.84.77.194",
                            60017,
                            "srflx",
                            mapOf(
                                "raddr" to "192.168.34.75",
                                "rport" to "60017",
                                "tcptype" to "passive",
                                "generation" to "0",
                                "network-id" to "3",
                                "network-cost" to "10"
                            )
                        ),
                        EndOfCandidatesAttribute,
                        RemoteCandidateAttribute.of("1 203.0.113.1 54400 2 203.0.113.1 54401"),
                        IceOptionsAttribute.of("google-ice"),
                        SsrcAttribute.of(2566107569L, "cname", "t9YU8M1UxTF8Y1A1"),
                        SsrcAttribute.of(2566107569L, "cname"),
                        SsrcGroupAttribute.of("FEC", 1, 2),
                        SsrcGroupAttribute.of("FEC-FR", 3004364195L, 1080772241L),
                        MsidSemanticAttribute.of("WMS", "Jvlam5X3SX1OP6pn20zWogvaKJz5Hjf9OnlV"),
                        MsidSemanticAttribute.of("WMS"),
                        GroupAttribute.of("BUNDLE", "audio", "video"),
                        RTCPMuxAttribute.of(),
                        RTCPRsizeAttribute.of(),
                        SctpMapAttribute.of(5000, "webrtc-datachannel", 1024),
                        XgoogleFlagAttribute.of("conference"),
                        RidAttribute.of("1", StreamDirection.SEND, "max-width=1280;max-height=720;max-fps=30;depend=0"),
                        ImageAttrsAttribute.of(
                            "97",
                            "send",
                            "[x=800,y=640,sar=1.1,q=0.6] [x=480,y=320]",
                            "recv",
                            "[x=330,y=250]"
                        ),
                        ImageAttrsAttribute.of("*", "send", "[x=800,y=640]", "recv", "*"),
                        ImageAttrsAttribute.of("100", "recv", "[x=320,y=240]"),
                        SimulcastAttribute.of(StreamDirection.SEND, "1,2,3;~4,~5", StreamDirection.RECV, "6;~7,~8"),
                        SimulcastAttribute.of(StreamDirection.RECV, "1;4,5", StreamDirection.SEND, "6;7"),
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

        actual.toString() shouldBe expected.toString()
        actual.toString() shouldBe expectedStr
            .replace(";\\s+".toRegex(), ";")
            .replace("^a=mid:\\s+".toRegex(RegexOption.MULTILINE), "a=mid:")
            .replace("^a=ice-ufrag:\\s+".toRegex(RegexOption.MULTILINE), "a=ice-ufrag:")
            .replace("^a=ice-pwd:\\s+".toRegex(RegexOption.MULTILINE), "a=ice-pwd:")

        val newMediaDescription = SdpMediaDescription.of(
            "audio", 49170, null, listOf("RTP", "AVP"), listOf("0"),
            attributes = listOf(
                MidAttribute.of("123"),
                SendOnlyAttribute,
                RTCPAttribute.of(65179, "IN", "IP4", "123.45.67.89"),
                RTCPAttribute.of(12345),
                ControlAttribute.of("streamId=0"),
                RTCPFbAttribute.of("98", "trr-int", "100"),
                RTCPFbAttribute.of("98", "nack", "rpsi"),
                ExtMapAttribute.of(2, uri = "urn:ietf:params:rtp-hdrext:toffset"),
                ExtMapAttribute.of(1, Direction.RECVONLY, uri = "URI-gps-string"),
                ExtMapAttribute.of(3, null, "urn:ietf:params:rtp-hdrext:encrypt", "urn:ietf:params:rtp-hdrext:smpte-tc", "25@600/24"),
                FormatAttribute.of(98).also {
                    it.addParameter("minptime", 10)
                    it.addParameter("useinbandfec", 1)
                }
            ))

        expected.getMediaDescription("123")?.setAttribute(SendOnlyAttribute, DirectionAttribute::class)
        actual.setMediaDescription(newMediaDescription, "123")
        actual.toString() shouldBe expected.toString()
    }

    "IP4データパース" {
        val expected = File("src/test/data/ip4_parse_test.sdp").readText().lines().joinToString("\r\n")
        val actual = SdpSessionDescription.parse(expected)
        actual.toString() shouldBe expected
    }

    "IP6データパース" {
        val expected = File("src/test/data/ip6_parse_test.sdp").readText().lines().joinToString("\r\n")

        val actual = SdpSessionDescription.parse(expected)
        actual.toString() shouldBe expected
    }

    "setMediaDescription" {
        val sessionDescription = SdpSessionDescription.of(
            version = SdpVersion.of(0),
            sessionName = SdpSessionName.of("a"),
            origin = SdpOrigin.of("-", BigInteger.ONE, BigInteger.ONE, "IN", "IP4", "0.0.0.0")
        )

        // need empty
        sessionDescription.getMediaDescriptions().count() shouldBe 0

        // add mid=1
        sessionDescription.addMediaDescription(SdpMediaDescription.of("audio", 1).also { it.mid = "1" })
        sessionDescription.asClue {
            it.getMediaDescriptions().count() shouldBe 1
            it.getMediaDescription("1")?.mid shouldBe "1"
        }

        // add mid=2
        sessionDescription.addMediaDescription(SdpMediaDescription.of("video", 1).also { it.mid = "2" })
        sessionDescription.asClue {
            it.getMediaDescriptions().count() shouldBe 2
            it.getMediaDescription("2")?.mid shouldBe "2"
        }

        // add mid=2 (ex. RTP)
        sessionDescription.addMediaDescription(SdpMediaDescription.of("video", 1).also { it.mid = "2" })
        sessionDescription.asClue {
            it.getMediaDescriptions().count() shouldBe 3
            it.getMediaDescription("2")?.mid shouldBe "2"
        }

        // replace mid=1 to mid=3
        sessionDescription.setMediaDescription(SdpMediaDescription.of("audio", 1).also { it.mid = "3" }, "1")
        sessionDescription.asClue {
            it.getMediaDescriptions().count() shouldBe 3
            it.getMediaDescription("1")?.mid shouldBe null
            it.getMediaDescription("3")?.mid shouldBe "3"
        }

        // replace mid=3 to mid=4
        sessionDescription.setMediaDescription(SdpMediaDescription.of("audio", 1).also { it.mid = "4" }, "3")
        sessionDescription.asClue {
            it.getMediaDescriptions().count() shouldBe 3
            it.getMediaDescription("1")?.mid shouldBe null
            it.getMediaDescription("3")?.mid shouldBe null
            it.getMediaDescription("4")?.mid shouldBe "4"
        }

        // replace mid=1 to mid=5 (add mid=5)
        sessionDescription.setMediaDescription(SdpMediaDescription.of("audio", 1).also { it.mid = "5" }, "1")
        sessionDescription.asClue {
            it.getMediaDescriptions().count() shouldBe 4
            it.getMediaDescription("1")?.mid shouldBe null
            it.getMediaDescription("3")?.mid shouldBe null
            it.getMediaDescription("4")?.mid shouldBe "4"
            it.getMediaDescription("5")?.mid shouldBe "5"
            it.getMediaDescriptions().joinToString(",") { desc -> "${desc.type}:${desc.mid}" } shouldBe "audio:4,video:2,video:2,audio:5"
        }
    }
})
