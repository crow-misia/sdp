package io.github.crow_misia.sdp.attribute

data class Simulcast03Attribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "simulcast"

        @JvmStatic
        fun of(value: String): Simulcast03Attribute {
            return Simulcast03Attribute(" $value")
        }
    }
}
