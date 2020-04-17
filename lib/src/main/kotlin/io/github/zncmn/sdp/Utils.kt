package io.github.zncmn.sdp

import io.github.zncmn.sdp.attribute.*

internal object Utils {
    private val PARSERS: Map<String, (String) -> SdpAttribute> = mapOf(
        CandidateAttribute.FIELD_NAME to { v -> CandidateAttribute.parse(v) },
        CNameAttribute.FIELD_NAME to { v -> CNameAttribute.parse(v) },
        ControlAttribute.FIELD_NAME to { v -> ControlAttribute.parse(v) },
        CryptoAttribute.FIELD_NAME to { v -> CryptoAttribute.parse(v) },
        DirectionAttribute.FIELD_NAME_SENDONLY to { _ -> SendOnlyAttribute },
        DirectionAttribute.FIELD_NAME_RECVONLY to { _ -> RecvOnlyAttribute },
        DirectionAttribute.FIELD_NAME_SENDRECV to { _ -> SendRecvAttribute },
        DirectionAttribute.FIELD_NAME_INACTIVE to { _ -> InactiveAttribute },
        EndOfCandidatesAttribute.FIELD_NAME to { _ -> EndOfCandidatesAttribute },
        FingerprintAttribute.FIELD_NAME to { v -> FingerprintAttribute.parse(v) },
        FormatAttribute.FIELD_NAME to { v -> FormatAttribute.parse(v) },
        IceLiteAttribute.FIELD_NAME to { _ -> IceLiteAttribute },
        IceUfragAttribute.FIELD_NAME to { v -> IceUfragAttribute.parse(v) },
        IcePwdAttribute.FIELD_NAME to { v -> IcePwdAttribute.parse(v) },
        MidAttribute.FIELD_NAME to { v -> MidAttribute.parse(v) },
        RTCPAttribute.FIELD_NAME to { v -> RTCPAttribute.parse(v) },
        RTPMapAttribute.FIELD_NAME to { v -> RTPMapAttribute.parse(v) },
        SetupAttribute.FIELD_NAME to { v -> SetupAttribute.parse(v) }
    )

    @JvmStatic
    fun parseAttribute(line: String): SdpAttribute {
        val colonIndex = line.indexOf(':', 2)
        val (field, value) = if (colonIndex < 0) {
            line.substring(2) to ""
        } else {
            line.substring(2, colonIndex) to line.substring(colonIndex + 1)
        }

        val lowerField = SdpAttribute.getFieldName(field)
        return PARSERS[lowerField]?.invoke(value) ?: run {
            BaseSdpAttribute.of(lowerField, value)
        }
    }
}