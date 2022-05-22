package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpElement
import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.13. Attributes.
 * a=<attribute-name>
 * a=<attribute-name>:<attribute-value>
 */
abstract class SdpAttribute : SdpElement() {
    abstract val field: String

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        append(field)
        valueJoinTo(this)
        appendSdpLineSeparator()
    }

    protected abstract fun valueJoinTo(buffer: StringBuilder): StringBuilder

    companion object {
        internal const val fieldPart = "a="
    }
}
