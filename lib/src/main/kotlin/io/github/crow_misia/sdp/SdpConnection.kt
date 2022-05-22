package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator

/**
 * RFC 8866 5.7. Connection Information.
 * c=<nettype> <addtype> <connection-address>
 * <connection-address>=<base multicast address>[/<ttl>]/<number of addresses>
 */
data class SdpConnection internal constructor(
    var nettype: String,
    var addrtype: String,
    var connectionAddress: String,
    var ttl: Int?,
    var numberOfAddresses: Int,
) : SdpElement() {
    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
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
        appendSdpLineSeparator()
    }

    companion object {
        internal const val fieldPart = "c="

        @JvmStatic
        @JvmOverloads
        fun of(
            nettype: String,
            addrtype: String,
            connectionAddress: String,
            ttl: Int? = null,
            numberOfAddresses: Int = 1,
        ): SdpConnection {
            return SdpConnection(nettype, addrtype, connectionAddress, ttl, numberOfAddresses)
        }

        internal fun parse(line: String): SdpConnection {
            val values = line.substring(2).split(' ')
            if (values.size != 3) {
                throw SdpParseException("could not parse: $line as Connection")
            }
            val addrtype = values[1]
            val connectionAddress = values[2]
            val subFields = connectionAddress.split('/')
            // the TTL subfield is not present in "IP6" multicast
            val baseMuticastAddress = subFields[0]
            val (ttl, numberOfAddresses) = if (subFields.size > 1) {
                val v = subFields[1].toIntOrNull() ?: run {
                    throw SdpParseException("could not parse: $line as Connection")
                }
                if (addrtype == "IP6" || subFields.size == 2) {
                    null to v
                } else {
                    v to 1
                }
            } else {
                null to 1
            }
            return SdpConnection(
                nettype = values[0],
                addrtype = addrtype,
                connectionAddress = baseMuticastAddress,
                ttl = ttl,
                numberOfAddresses = numberOfAddresses,
            )
        }
    }
}
