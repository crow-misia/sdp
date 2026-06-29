package io.github.crow_misia.sdp.attribute

object EndOfCandidatesAttribute : SdpAttribute() {
    internal const val FIELD_NAME = "end-of-candidates"

    override val field = FIELD_NAME

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
