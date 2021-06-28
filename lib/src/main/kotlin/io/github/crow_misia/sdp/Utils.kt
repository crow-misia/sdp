package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.*
import java.util.*

@Suppress("NOTHING_TO_INLINE")
internal object Utils {
    inline fun StringBuilder.appendSdpLineSeparator(): StringBuilder = append("\r\n")

    private val PARSERS: Map<String, (String) -> SdpAttribute> = hashMapOf(
        CandidateAttribute.fieldName to { v -> CandidateAttribute.parse(v) },
        CNameAttribute.fieldName to { v -> CNameAttribute.parse(v) },
        ControlAttribute.fieldName to { v -> ControlAttribute.parse(v) },
        CryptoAttribute.fieldName to { v -> CryptoAttribute.parse(v) },
        SendOnlyAttribute.field to { SendOnlyAttribute },
        RecvOnlyAttribute.field to { RecvOnlyAttribute },
        SendRecvAttribute.field to { SendRecvAttribute },
        InactiveAttribute.field to { InactiveAttribute },
        EndOfCandidatesAttribute.fieldName to { EndOfCandidatesAttribute },
        ExtMapAttribute.fieldName to { v -> ExtMapAttribute.parse(v) },
        ExtmapAllowMixedAttribute.fieldName to { ExtmapAllowMixedAttribute },
        FingerprintAttribute.fieldName to { v -> FingerprintAttribute.parse(v) },
        FormatAttribute.fieldName to { v -> FormatAttribute.parse(v) },
        FramerateAttribute.fieldName to { v -> FramerateAttribute.parse(v) },
        GroupAttribute.fieldName to { v -> GroupAttribute.parse(v) },
        IceLiteAttribute.fieldName to { IceLiteAttribute },
        IceOptionsAttribute.fieldName to { v -> IceOptionsAttribute.parse(v) },
        IceUfragAttribute.fieldName to { v -> IceUfragAttribute.parse(v) },
        IcePwdAttribute.fieldName to { v -> IcePwdAttribute.parse(v) },
        ImageAttrsAttribute.fieldName to { v -> ImageAttrsAttribute.parse(v) },
        MaxMessageSizeAttribute.fieldName to { v -> MaxMessageSizeAttribute.parse(v) },
        MaxPtimeAttribute.fieldName to { v -> MaxPtimeAttribute.parse(v) },
        MediaclkAttribute.fieldName to { v -> MediaclkAttribute.parse(v) },
        MidAttribute.fieldName to { v -> MidAttribute.parse(v) },
        MsidAttribute.fieldName to { v -> MsidAttribute.parse(v) },
        MsidSemanticAttribute.fieldName to { v -> MsidSemanticAttribute.parse(v) },
        PtimeAttribute.fieldName to { v -> PtimeAttribute.parse(v) },
        RemoteCandidateAttribute.fieldName to { v -> RemoteCandidateAttribute.parse(v) },
        RidAttribute.fieldName to { v -> RidAttribute.parse(v) },
        RTCPAttribute.fieldName to { v -> RTCPAttribute.parse(v) },
        RTCPFbAttribute.fieldName to { v -> RTCPFbAttribute.parse(v) },
        RTCPMuxAttribute.fieldName to { RTCPMuxAttribute },
        RTCPRsizeAttribute.fieldName to { RTCPRsizeAttribute },
        RTPMapAttribute.fieldName to { v -> RTPMapAttribute.parse(v) },
        SctpMapAttribute.fieldName to { v -> SctpMapAttribute.parse(v) },
        SctpPortAttribute.fieldName to { v -> SctpPortAttribute.parse(v) },
        SimulcastAttribute.fieldName to { v -> SimulcastAttribute.parse(v) },
        SourceFilterAttribute.fieldName to { v -> SourceFilterAttribute.parse(v) },
        SetupAttribute.fieldName to { v -> SetupAttribute.parse(v) },
        SsrcAttribute.fieldName to { v -> SsrcAttribute.parse(v) },
        SsrcGroupAttribute.fieldName to { v -> SsrcGroupAttribute.parse(v) },
        TsRefclkAttribute.fieldName to { v -> TsRefclkAttribute.parse(v) },
        XgoogleFlagAttribute.fieldName to { v -> XgoogleFlagAttribute.parse(v) }
    )

    init {
        assert(PARSERS.size == 43)
    }

    internal inline fun getFieldName(field: String?) = field?.lowercase(Locale.ENGLISH).orEmpty()

    internal inline fun getName(name: String?) = name?.lowercase(Locale.ENGLISH).orEmpty()

    @JvmStatic
    fun parseAttribute(line: String): SdpAttribute {
        val colonIndex = line.indexOf(':', 2)
        val (field, value) = if (colonIndex < 0) {
            line.substring(2) to ""
        } else {
            line.substring(2, colonIndex) to line.substring(colonIndex + 1)
        }

        val lowerField = getFieldName(field)
        return PARSERS[lowerField]?.invoke(value) ?: run {
            BaseSdpAttribute.of(lowerField, value)
        }
    }
}
