package io.github.zncmn.sdp.attribute

data class SimulcastAttribute internal constructor(
    override var value: String?
) : BaseSdpAttribute(FIELD_NAME, value) {

    companion object {
        internal const val FIELD_NAME = "simulcast"

        @JvmStatic
        fun of(value: String? = null): SimulcastAttribute {
            return SimulcastAttribute(value)
        }

        internal fun parse(value: String): SimulcastAttribute {
            return if (value.isEmpty())
                SimulcastAttribute(null)
            else
                SimulcastAttribute(value)
        }
    }
}