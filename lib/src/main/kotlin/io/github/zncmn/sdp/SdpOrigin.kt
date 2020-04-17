package io.github.zncmn.sdp

data class SdpOrigin internal constructor(
    var username: String,
    var sessId: Long,
    var sessVersion: Long,
    var nettype: String,
    var addrtype: String,
    var unicastAddress: String
) : SdpElement {
    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("o=")
            append(username)
            append(' ')
            append(sessId)
            append(' ')
            append(sessVersion)
            append(' ')
            append(nettype)
            append(' ')
            append(addrtype)
            append(' ')
            append(unicastAddress)
            append("\r\n")
        }
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(username: String = "-",
               sessId: Long,
               sessVersion: Long,
               nettype: String,
               addrtype: String,
               unicastAddress: String
        ): SdpOrigin {
            return SdpOrigin(username, sessId, sessVersion, nettype, addrtype, unicastAddress)
        }

        internal fun parse(line: String): SdpOrigin {
            val values = line.substring(2).split(' ')
            if (values.size != 6) {
                throw SdpParseException("could not parse: $line as Origin")
            }
            val id = values[1].toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $line as Origin")
            }
            val version = values[2].toLongOrNull() ?: run {
                throw SdpParseException("could not parse: $line as Origin")
            }

            return SdpOrigin(values[0], id, version, values[3], values[4], values[5])
        }
    }
}
