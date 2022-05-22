package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

/**
 * RFC8866 6.8. orient (Orientation)
 * Name: orient
 * Value: orient-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * orient-value = portrait / landscape / seascape
 * portrait  = %s"portrait"
 * landscape = %s"landscape"
 * seascape  = %s"seascape"
 * ; NOTE: These names are case-sensitive.
 * Example:
 * a=orient:portrait
 */
data class OrientAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(fieldName, value) {
    companion object {
        internal const val fieldName = "orient"

        @JvmStatic
        fun of(value: String): OrientAttribute {
            return OrientAttribute(value)
        }

        internal fun parse(value: String): OrientAttribute {
            return OrientAttribute(value)
        }
    }
}
