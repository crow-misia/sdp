package io.github.zncmn.sdp.mediasoup

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IceParameters(
    /**
     * ICE username fragment.
     */
    var usernameFragment: String,

    /**
     * ICE password.
     */
    var password: String,

    /**
     * ICE Lite.
     */
    var iceLite: Boolean = false
)

@JsonClass(generateAdapter = true)
data class IceCandidate(
    /**
     * Unique identifier that allows ICE to correlate candidates that appear on
     * multiple transports.
     */
    var foundation: String,

    /**
     * The assigned priority of the candidate.
     */
    var priority: Long,

    /**
     * The IP address of the candidate.
     */
    var ip: String,

    /**
     * The protocol of the candidate.
     */
    var protocol: String,

    /**
     * The port for the candidate.
     */
    var port: Int,

    /**
     * The type of candidate..
     */
    var type: String,

    /**
     * The type of TCP candidate.
     */
    var tcpType: String? = null
)

@JsonClass(generateAdapter = true)
data class DtlsParameters(
    /**
     * DTLS role. Default 'auto'.
     */
    var role: DtlsRole?,

    /**
     * DTLS fingerprints.
     */
    var fingerprints: List<DtlsFingerprint> = emptyList()
)

@JsonClass(generateAdapter = true)
data class DtlsFingerprint(
    var algorithm: String,
    var value: String
)

enum class DtlsRole {
    @Json(name = "auto") AUTO,
    @Json(name = "client") CLIENT,
    @Json(name = "server") SERVER
}


enum class ConnectionState {
    @Json(name = "new") NEW,
    @Json(name = "connecting") CONNECTING,
    @Json(name = "connected") CONNECTED,
    @Json(name = "failed") FAILED,
    @Json(name = "disconnected") DISCONNECTED,
    @Json(name = "closed") CLOSED
}
