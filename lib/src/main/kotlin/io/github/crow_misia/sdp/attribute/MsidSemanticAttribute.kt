package io.github.crow_misia.sdp.attribute

data class MsidSemanticAttribute internal constructor(
    var semantic: String,
    var token: String,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(": ")
        append(semantic)
        if (token.isNotEmpty()) {
            append(' ')
            append(token)
        }
    }

    companion object {
        internal const val fieldName = "msid-semantic"

        @JvmStatic @JvmOverloads
        fun of(semantic: String, token: String? = null): MsidSemanticAttribute {
            return MsidSemanticAttribute(semantic, token.orEmpty())
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.trimStart().split(' ', limit = 2)
            return MsidSemanticAttribute(values[0], if (values.size > 1) values[1] else "")
        }
    }
}
