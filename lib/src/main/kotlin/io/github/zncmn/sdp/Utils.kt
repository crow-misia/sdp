package io.github.zncmn.sdp

import io.github.zncmn.sdp.attribute.*

internal object Utils {
    private val PARSERS: Map<String, (String) -> SdpAttribute> = hashMapOf(
        CandidateAttribute.FIELD_NAME to { v -> CandidateAttribute.parse(v) },
        CNameAttribute.FIELD_NAME to { v -> CNameAttribute.parse(v) },
        ControlAttribute.FIELD_NAME to { v -> ControlAttribute.parse(v) },
        CryptoAttribute.FIELD_NAME to { v -> CryptoAttribute.parse(v) },
        SendOnlyAttribute.field to { _ -> SendOnlyAttribute },
        RecvOnlyAttribute.field to { _ -> RecvOnlyAttribute },
        SendRecvAttribute.field to { _ -> SendRecvAttribute },
        InactiveAttribute.field to { _ -> InactiveAttribute },
        EndOfCandidatesAttribute.FIELD_NAME to { _ -> EndOfCandidatesAttribute },
        ExtMapAttribute.FIELD_NAME to { v -> ExtMapAttribute.parse(v) },
        FingerprintAttribute.FIELD_NAME to { v -> FingerprintAttribute.parse(v) },
        FormatAttribute.FIELD_NAME to { v -> FormatAttribute.parse(v) },
        FramerateAttribute.FIELD_NAME to { v -> FramerateAttribute.parse(v) },
        GroupAttribute.FIELD_NAME to { v -> GroupAttribute.parse(v) },
        IceLiteAttribute.FIELD_NAME to { _ -> IceLiteAttribute },
        IceOptionsAttribute.FIELD_NAME to { v -> IceOptionsAttribute.parse(v) },
        IceUfragAttribute.FIELD_NAME to { v -> IceUfragAttribute.parse(v) },
        IcePwdAttribute.FIELD_NAME to { v -> IcePwdAttribute.parse(v) },
        MaxPtimeAttribute.FIELD_NAME to { v -> MaxPtimeAttribute.parse(v) },
        MediaclkAttribute.FIELD_NAME to { v -> MediaclkAttribute.parse(v) },
        MidAttribute.FIELD_NAME to { v -> MidAttribute.parse(v) },
        MsidAttribute.FIELD_NAME to { v -> MsidAttribute.parse(v) },
        MsidSemanticAttribute.FIELD_NAME to { v -> MsidSemanticAttribute.parse(v) },
        PtimeAttribute.FIELD_NAME to { v -> PtimeAttribute.parse(v) },
        RidAttribute.FIELD_NAME to { v -> RidAttribute.parse(v) },
        RTCPAttribute.FIELD_NAME to { v -> RTCPAttribute.parse(v) },
        RTCPFbAttribute.FIELD_NAME to { v -> RTCPFbAttribute.parse(v) },
        RTCPMuxAttribute.FIELD_NAME to { _ -> RTCPMuxAttribute },
        RTCPRsizeAttribute.FIELD_NAME to { _ -> RTCPRsizeAttribute },
        RTPMapAttribute.FIELD_NAME to { v -> RTPMapAttribute.parse(v) },
        SctpMapAttribute.FIELD_NAME to { v -> SctpMapAttribute.parse(v) },
        SetupAttribute.FIELD_NAME to { v -> SetupAttribute.parse(v) },
        SsrcAttribute.FIELD_NAME to { v -> SsrcAttribute.parse(v) },
        SsrcGroupAttribute.FIELD_NAME to { v -> SsrcGroupAttribute.parse(v) },
        TsRefclkAttribute.FIELD_NAME to { v -> TsRefclkAttribute.parse(v) },
        XgoogleFlagAttribute.FIELD_NAME to { v -> XgoogleFlagAttribute.parse(v) }
    )

    init {
        assert(PARSERS.size == 36)
    }

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