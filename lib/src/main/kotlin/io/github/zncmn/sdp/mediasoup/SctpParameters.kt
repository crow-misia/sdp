package io.github.zncmn.sdp.mediasoup

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.github.zncmn.sdp.webrtc.RTCPriorityType

@JsonClass(generateAdapter = true)
data class SctpCapabilities @JvmOverloads constructor(
    var numStreams: List<NumSctpStreams> = emptyList()
)

@JsonClass(generateAdapter = true)
data class NumSctpStreams(
    /**
     * Initially requested number of outgoing SCTP streams.
     */
    @Json(name = "OS") var os: Int,

    /**
     * Maximum number of incoming SCTP streams.
     */
    @Json(name = "MIS") var mis: Int
)

@JsonClass(generateAdapter = true)
data class SctpParameters @JvmOverloads constructor(
    /**
     * Must always equal 5000.
     */
    var port: Int = 5000,

    /**
     * Initially requested number of outgoing SCTP streams.
     */
    @Json(name = "OS") var os: Int,

    /**
     * Maximum number of incoming SCTP streams.
     */
    @Json(name = "MIS") var mis: Int,

    /**
     * Maximum allowed size for SCTP messages.
     */
    var maxMessageSize: Int
)

@JsonClass(generateAdapter = true)
data class SctpStreamParameters @JvmOverloads constructor(
    /**
     * SCTP stream id.
     */
    var streamId: Int,

    /**
     * Whether data messages must be received in order. if true the messages will
     * be sent reliably. Default true.
     */
    var ordered: Boolean = true,

    /**
     * When ordered is false indicates the time (in milliseconds) after which a
     * SCTP packet will stop being retransmitted.
     */
    var maxPacketLifeTime: Int = 0,

    /**
     * When ordered is false indicates the maximum number of times a packet will
     * be retransmitted.
     */
    var maxRetransmits: Int = 0,

    /**
     * DataChannel priority.
     */
    var priority: RTCPriorityType? = null,

    /**
     * A label which can be used to distinguish this DataChannel from others.
     */
    var label: String = "",

    /**
     * Name of the sub-protocol used by this DataChannel.
     */
    var protocol: String = ""
)
