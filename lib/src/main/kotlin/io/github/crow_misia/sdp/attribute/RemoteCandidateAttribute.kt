package io.github.crow_misia.sdp.attribute

data class RemoteCandidateAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder): StringBuilder {
        if (value.isEmpty()) {
            return buffer
        }
        return super.joinTo(buffer)
    }

    companion object {
        internal const val fieldName = "remote-candidates"

        @JvmStatic
        fun of(value: String): RemoteCandidateAttribute {
            return RemoteCandidateAttribute(value.trim())
        }

        internal fun parse(value: String): SdpAttribute {
            return RemoteCandidateAttribute(value)
        }
    }
}
