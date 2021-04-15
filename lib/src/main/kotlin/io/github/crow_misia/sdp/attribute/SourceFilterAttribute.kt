package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException

data class SourceFilterAttribute internal constructor(
    var filterMode: String,
    var netType: String,
    var addressTypes: String,
    var destAddress: String,
    val srcList: MutableSet<String>
) : SdpAttribute {
    override val field = FIELD_NAME

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append(':')
            valueJoinTo(this)
            append("\r\n")
        }
    }

    private fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
            append(' ')
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
    }

    companion object {
        internal const val FIELD_NAME = "source-filter"

        @JvmStatic
        fun of(filterMode: String, netType: String, addressTypes: String, destAddress: String, vararg srcAddress: String): SourceFilterAttribute {
            return SourceFilterAttribute(filterMode, netType, addressTypes, destAddress, srcAddress.toMutableSet())
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.trimStart().split(' ')
            val size = values.size
            if (size < 4) {
                throw SdpParseException("could not parse: $value as SourceFilterAttribute")
            }
            return SourceFilterAttribute(
                filterMode = values[0],
                netType = values[1],
                addressTypes = values[2],
                destAddress = values[3],
                srcList = values.subList(4, size).toMutableSet()
            )
        }
    }
}