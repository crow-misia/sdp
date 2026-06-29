package io.github.crow_misia.sdp.attribute

/**
 * RFC8866 6.12. lang (Language)
 * Name: lang
 * Value: lang-value
 * Usage Level: media
 * Charset Dependent: no
 * Syntax:
 * lang-value = Language-Tag ; Language-Tag defined in RFC 5646
 * Example:
 * a=lang:de
 */
data class LangAttribute internal constructor(
    override var value: String,
) : BaseSdpAttribute(FIELD_NAME, value) {
    companion object {
        internal const val FIELD_NAME = "lang"

        @JvmStatic
        fun of(value: String): LangAttribute {
            return LangAttribute(value)
        }

        internal fun parse(value: String): LangAttribute {
            return LangAttribute(value)
        }
    }
}
