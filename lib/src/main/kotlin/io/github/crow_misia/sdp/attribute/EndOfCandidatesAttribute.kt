package io.github.crow_misia.sdp.attribute

object EndOfCandidatesAttribute : SdpAttribute() {
    internal const val fieldName = "end-of-candidates"

    override val field = fieldName

    override fun valueJoinTo(buffer: StringBuilder) = buffer

    @JvmStatic
    fun of() = this
}
