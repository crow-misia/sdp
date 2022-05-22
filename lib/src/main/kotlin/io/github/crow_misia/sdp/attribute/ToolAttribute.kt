@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp.attribute

/**
 * RFC8866 6.3. tool
 * Name: tool
 * Value: tool-value
 * Usage Level: session
 * Charset Dependent: no
 * Syntax:
 * tool-value = tool-name-and-version tool-name-and-version = text
 * Example:
 * a=tool:foobar V3.2
 */
data class ToolAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(fieldName, value) {
    override fun toString() = super.toString()

    companion object {
        internal const val fieldName = "tool"

        @JvmStatic
        fun of(value: String): ToolAttribute {
            return ToolAttribute(value)
        }

        internal fun parse(value: String): ToolAttribute {
            return ToolAttribute(value)
        }
    }
}
