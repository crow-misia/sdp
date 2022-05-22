package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator
import java.math.BigInteger

/**
 * RFC 8866 5.2. Origin.
 * o=<username> <sess-id> <sess-version> <nettype> <addrtype> <unicast-address>
 */
data class SdpOrigin internal constructor(
    var username: String,
    var sessId: BigInteger,
    var sessVersion: BigInteger,
    var nettype: String,
    var addrtype: String,
    var unicastAddress: String,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(lineType)
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
        appendSdpLineSeparator()
    }

    companion object {
        internal const val lineType = "o="

        @JvmStatic
        @JvmOverloads
        fun of(
            username: String = "-",
            sessId: BigInteger,
            sessVersion: BigInteger,
            nettype: String,
            addrtype: String,
            unicastAddress: String,
        ): SdpOrigin {
            return SdpOrigin(
                username = username,
                sessId = sessId,
                sessVersion = sessVersion,
                nettype = nettype,
                addrtype = addrtype,
                unicastAddress = unicastAddress,
            )
        }

        internal fun parse(line: String): SdpOrigin {
            val values = line.substring(2).split(' ')
            if (values.size != 6) {
                throw SdpParseException("could not parse: $line as Origin")
            }
            val id = values[1].toBigIntegerOrNull() ?: run {
                throw SdpParseException("could not parse: $line as Origin")
            }
            val version = values[2].toBigIntegerOrNull() ?: run {
                throw SdpParseException("could not parse: $line as Origin")
            }

            return SdpOrigin(
                username = values[0],
                sessId = id,
                sessVersion = version,
                nettype = values[3],
                addrtype = values[4],
                unicastAddress = values[5],
            )
        }
    }
}
