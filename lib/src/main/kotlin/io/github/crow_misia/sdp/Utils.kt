package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.*
import java.util.*
import kotlin.math.absoluteValue

@Suppress("NOTHING_TO_INLINE")
internal object Utils {
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
        FmtpAttribute.fieldName to { v -> FmtpAttribute.parse(v) },
        FramerateAttribute.fieldName to { v -> FramerateAttribute.parse(v) },
        GroupAttribute.fieldName to { v -> GroupAttribute.parse(v) },
        IceLiteAttribute.fieldName to { IceLiteAttribute },
        IceMismatchAttribute.fieldName to { IceMismatchAttribute },
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
        XgoogleFlagAttribute.fieldName to { v -> XgoogleFlagAttribute.parse(v) },
        ToolAttribute.fieldName to { v -> ToolAttribute.parse(v) },
        OrientAttribute.fieldName to { v -> OrientAttribute.parse(v) },
        TypeAttribute.fieldName to { v -> TypeAttribute.parse(v) },
        CharsetAttribute.fieldName to { v -> CharsetAttribute.parse(v) },
        SdplangAttribute.fieldName to { v -> SdplangAttribute.parse(v) },
        QualityAttribute.fieldName to { v -> QualityAttribute.parse(v) },
    )

    init {
        assert(PARSERS.size == 50)
    }

    inline fun StringBuilder.appendSdpLineSeparator(): StringBuilder = append("\r\n")

    internal inline fun getFieldName(field: String?) = field?.lowercase(Locale.ENGLISH).orEmpty()

    internal inline fun getName(name: String?) = name?.lowercase(Locale.ENGLISH).orEmpty()

    /**
     * Returns String with omitted decimal part if it's zero. E.g. 42.0 -> "42", 42.4 -> "42.4".
     */
    internal fun Double.toCompactString() = if (this.rem(1.0).absoluteValue > 0.0) {
        this.toString()
    } else {
        this.toLong().toString()
    }

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
