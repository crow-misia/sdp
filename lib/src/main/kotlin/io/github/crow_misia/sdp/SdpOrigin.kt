package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator
import java.math.BigInteger

data class SdpOrigin internal constructor(
    val username: String,
    val sessId: BigInteger,
    val sessVersion: BigInteger,
    val nettype: String,
    val addrtype: String,
    val unicastAddress: String,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
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
        appendSdpLineSeparator()
    }

    companion object {
        internal const val fieldPart = "o="

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
            return SdpOrigin(username, sessId, sessVersion, nettype, addrtype, unicastAddress)
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

            return SdpOrigin(values[0], id, version, values[3], values[4], values[5])
        }
    }
}
