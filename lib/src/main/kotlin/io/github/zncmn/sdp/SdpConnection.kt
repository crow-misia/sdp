package io.github.zncmn.sdp

data class SdpConnection internal constructor(
    var nettype: String,
    var addrtype: String,
    var connectionAddress: String,
    var ttl: Int?,
    var numberOfAddresses: Int
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("c=")
            append(nettype)
            append(' ')
            append(addrtype)
            append(' ')
            append(connectionAddress)
            ttl?.also {
                append('/')
                append(it)
            }
            if (numberOfAddresses > 1) {
                append('/')
                append(numberOfAddresses)
            }
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(nettype: String,
               addrtype: String,
               connectionAddress: String,
               ttl: Int? = null,
               numberOfAddresses: Int = 1
        ): SdpConnection {
            return SdpConnection(nettype, addrtype, connectionAddress, ttl, numberOfAddresses)
        }

        internal fun parse(line: String): SdpConnection {
            val values = line.substring(2).split(' ')
            if (values.size != 3) {
                throw SdpParseException("could not parse: $line as Connection")
            }
            val tmp= values[2].split('/', limit = 2)
            val ttl = if (tmp.size > 1) {
                tmp[1].toIntOrNull() ?: run {
                    throw SdpParseException("could not parse: $line as Connection")
                }
            } else null

            return SdpConnection(values[0], values[1], tmp[0], ttl, 1)
        }
    }
}
