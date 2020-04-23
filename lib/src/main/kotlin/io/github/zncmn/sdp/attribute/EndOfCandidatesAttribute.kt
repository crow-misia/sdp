package io.github.zncmn.sdp.attribute

object EndOfCandidatesAttribute : SdpAttribute {
    internal const val FIELD_NAME = "end-of-candidates"

    override val field = FIELD_NAME

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append("\r\n")
        }
    }

    @JvmStatic
    fun of() = this
}