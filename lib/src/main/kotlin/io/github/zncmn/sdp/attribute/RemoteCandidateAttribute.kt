package io.github.zncmn.sdp.attribute

data class RemoteCandidateAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {
    override fun joinTo(buffer: StringBuilder) {
        if (value.isEmpty()) {
            return
        }
        super.joinTo(buffer)
    }

    companion object {
        internal const val FIELD_NAME = "remote-candidates"

        @JvmStatic
        fun of(value: String): RemoteCandidateAttribute {
            return RemoteCandidateAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return RemoteCandidateAttribute(value)
        }
    }
}