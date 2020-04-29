package io.github.zncmn.sdp.attribute

data class ControlAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {

    override fun joinTo(buffer: StringBuilder) {
        if (value.isBlank()) {
            return
        }
        super.joinTo(buffer)
    }

    companion object {
        internal const val FIELD_NAME = "control"

        @JvmStatic
        fun of(value: String): ControlAttribute {
            return ControlAttribute(value)
        }

        internal fun parse(value: String): SdpAttribute {
            return ControlAttribute(value)
        }
    }
}