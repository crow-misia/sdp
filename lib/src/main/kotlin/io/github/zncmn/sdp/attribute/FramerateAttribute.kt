package io.github.zncmn.sdp.attribute

data class FramerateAttribute internal constructor(
    override var value: String?
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "framerate"

        @JvmStatic
        fun of(value: Double): FramerateAttribute {
            return FramerateAttribute(value.toString())
        }

        internal fun parse(value: String): FramerateAttribute {
            return if (value.isEmpty())
                FramerateAttribute(null)
            else
                FramerateAttribute(value)
        }
    }
}