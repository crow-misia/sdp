package io.github.zncmn.sdp.attribute

data class Simulcast03Attribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "simulcast"

        @JvmStatic
        fun of(value: String): Simulcast03Attribute {
            return Simulcast03Attribute(" $value")
        }
    }
}