package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseContext
import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils.splitOnSpaces

data class SourceFilterAttribute internal constructor(
    var filterMode: String,
    var netType: String,
    var addressTypes: String,
    var destAddress: String,
    var srcList: MutableSet<String>,
) : SdpAttribute() {
    override val field = FIELD_NAME

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(": ")
        append(filterMode)
        append(' ')
        append(netType)
        append(' ')
        append(addressTypes)
        append(' ')
        append(destAddress)
        srcList.forEach {
            append(' ')
            append(it)
        }
    }

    companion object {
        internal const val FIELD_NAME = "source-filter"

        @JvmStatic
        fun of(
            filterMode: String,
            netType: String,
            addressTypes: String,
            destAddress: String,
            srcAddress: List<String>,
        ): SourceFilterAttribute {
            return SourceFilterAttribute(filterMode, netType, addressTypes, destAddress, srcAddress.toMutableSet())
        }

        @JvmStatic
        fun of(
            filterMode: String,
            netType: String,
            addressTypes: String,
            destAddress: String,
            vararg srcAddress: String,
        ): SourceFilterAttribute {
            return SourceFilterAttribute(filterMode, netType, addressTypes, destAddress, srcAddress.toMutableSet())
        }

        context(_: SdpParseContext)
        internal fun parse(value: String): SdpAttribute {
            val values = value.splitOnSpaces()
            val size = values.size
            if (size < 4) {
                throw SdpParseException("could not parse: $value as SourceFilterAttribute")
            }
            return SourceFilterAttribute(
                filterMode = values[0],
                netType = values[1],
                addressTypes = values[2],
                destAddress = values[3],
                srcList = values.subList(4, size).toMutableSet(),
            )
        }
    }
}
