package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpElement
import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

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
