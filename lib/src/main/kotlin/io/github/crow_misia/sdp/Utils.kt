package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.*
import java.util.*
import kotlin.math.absoluteValue

@Suppress("NOTHING_TO_INLINE")
internal object Utils {
    private val PARSERS: Map<String, context(SdpParseContext) (String) -> SdpAttribute> = hashMapOf(
        CandidateAttribute.FIELD_NAME to { v -> CandidateAttribute.parse(v) },
        CNameAttribute.FIELD_NAME to { v -> CNameAttribute.parse(v) },
        ControlAttribute.FIELD_NAME to { v -> ControlAttribute.parse(v) },
        CryptoAttribute.FIELD_NAME to { v -> CryptoAttribute.parse(v) },
        SendOnlyAttribute.field to { SendOnlyAttribute },
        RecvOnlyAttribute.field to { RecvOnlyAttribute },
        SendRecvAttribute.field to { SendRecvAttribute },
        InactiveAttribute.field to { InactiveAttribute },
        EndOfCandidatesAttribute.FIELD_NAME to { EndOfCandidatesAttribute },
        ExtMapAttribute.FIELD_NAME to { v -> ExtMapAttribute.parse(v) },
        ExtmapAllowMixedAttribute.FIELD_NAME to { ExtmapAllowMixedAttribute },
        FingerprintAttribute.FIELD_NAME to { v -> FingerprintAttribute.parse(v) },
        FmtpAttribute.FIELD_NAME to { v -> FmtpAttribute.parse(v) },
        FramerateAttribute.FIELD_NAME to { v -> FramerateAttribute.parse(v) },
        GroupAttribute.FIELD_NAME to { v -> GroupAttribute.parse(v) },
        IceLiteAttribute.FIELD_NAME to { IceLiteAttribute },
        IceMismatchAttribute.FIELD_NAME to { IceMismatchAttribute },
        IceOptionsAttribute.FIELD_NAME to { v -> IceOptionsAttribute.parse(v) },
        IceUfragAttribute.FIELD_NAME to { v -> IceUfragAttribute.parse(v) },
        IcePwdAttribute.FIELD_NAME to { v -> IcePwdAttribute.parse(v) },
        ImageAttrsAttribute.FIELD_NAME to { v -> ImageAttrsAttribute.parse(v) },
        MaxMessageSizeAttribute.FIELD_NAME to { v -> MaxMessageSizeAttribute.parse(v) },
        MaxPtimeAttribute.FIELD_NAME to { v -> MaxPtimeAttribute.parse(v) },
        MediaclkAttribute.FIELD_NAME to { v -> MediaclkAttribute.parse(v) },
        MidAttribute.FIELD_NAME to { v -> MidAttribute.parse(v) },
        MsidAttribute.FIELD_NAME to { v -> MsidAttribute.parse(v) },
        MsidSemanticAttribute.FIELD_NAME to { v -> MsidSemanticAttribute.parse(v) },
        PtimeAttribute.FIELD_NAME to { v -> PtimeAttribute.parse(v) },
        RemoteCandidateAttribute.FIELD_NAME to { v -> RemoteCandidateAttribute.parse(v) },
        RidAttribute.FIELD_NAME to { v -> RidAttribute.parse(v) },
        RTCPAttribute.FIELD_NAME to { v -> RTCPAttribute.parse(v) },
        RTCPFbAttribute.FIELD_NAME to { v -> RTCPFbAttribute.parse(v) },
        RTCPMuxAttribute.FIELD_NAME to { RTCPMuxAttribute },
        RTCPRsizeAttribute.FIELD_NAME to { RTCPRsizeAttribute },
        RTPMapAttribute.FIELD_NAME to { v -> RTPMapAttribute.parse(v) },
        SctpMapAttribute.FIELD_NAME to { v -> SctpMapAttribute.parse(v) },
        SctpPortAttribute.FIELD_NAME to { v -> SctpPortAttribute.parse(v) },
        SimulcastAttribute.FIELD_NAME to { v -> SimulcastAttribute.parse(v) },
        SourceFilterAttribute.FIELD_NAME to { v -> SourceFilterAttribute.parse(v) },
        SetupAttribute.FIELD_NAME to { v -> SetupAttribute.parse(v) },
        SsrcAttribute.FIELD_NAME to { v -> SsrcAttribute.parse(v) },
        SsrcGroupAttribute.FIELD_NAME to { v -> SsrcGroupAttribute.parse(v) },
        TsRefclkAttribute.FIELD_NAME to { v -> TsRefclkAttribute.parse(v) },
        XgoogleFlagAttribute.FIELD_NAME to { v -> XgoogleFlagAttribute.parse(v) },
        ToolAttribute.FIELD_NAME to { v -> ToolAttribute.parse(v) },
        OrientAttribute.FIELD_NAME to { v -> OrientAttribute.parse(v) },
        TypeAttribute.FIELD_NAME to { v -> TypeAttribute.parse(v) },
        CharsetAttribute.FIELD_NAME to { v -> CharsetAttribute.parse(v) },
        SdplangAttribute.FIELD_NAME to { v -> SdplangAttribute.parse(v) },
        QualityAttribute.FIELD_NAME to { v -> QualityAttribute.parse(v) },
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

    /**
     * Splits the receiver on the inter-field separator using the active
     * [SdpParseContext] (strict by default, lenient when parsing was started
     * with `strict = false`). See [SdpParseContext.splitOnSpaces] for [limit].
     */
    context(ctx: SdpParseContext)
    internal fun String.splitOnSpaces(startIndex: Int = 0, limit: Int = 0) = ctx.splitOnSpaces(input = this, startIndex = startIndex, limit = limit)

    context(ctx: SdpParseContext)
    internal fun parseAttribute(line: String): SdpAttribute {
        val colonIndex = line.indexOf(':', 2)
        val (field, value) = if (colonIndex < 0) {
            line.substring(2) to ""
        } else {
            line.substring(2, colonIndex) to line.substring(colonIndex + 1)
        }

        return with(SdpAttributeSdpContext(ctx)) {
            val lowerField = getFieldName(field)
            PARSERS[lowerField]?.invoke(this, value) ?: run {
                BaseSdpAttribute.of(lowerField, value)
            }
        }
    }
}
