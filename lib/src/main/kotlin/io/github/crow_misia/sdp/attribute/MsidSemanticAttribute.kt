package io.github.crow_misia.sdp.attribute

data class MsidSemanticAttribute internal constructor(
    var semantic: String,
    var token: String
) : SdpAttribute {
    override val field = FIELD_NAME

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append(':')
            valueJoinTo(this)
            append("\r\n")
        }
    }

    private fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
            append(' ')
            append(semantic)
            if (token.isNotEmpty()) {
                append(' ')
                append(token)
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "msid-semantic"

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