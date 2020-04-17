package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class MsidAttribute internal constructor(
    override var value: String?
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "msid"

        @JvmStatic
        fun of(value: String? = null): MsidAttribute {
            return MsidAttribute(value)
        }

        internal fun parse(value: String): MsidAttribute {
            return if (value.isEmpty())
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            else
                MsidAttribute(value)
        }
    }
}