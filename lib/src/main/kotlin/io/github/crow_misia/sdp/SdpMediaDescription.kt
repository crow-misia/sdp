@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.Utils.appendSdpLineSeparator
import io.github.crow_misia.sdp.attribute.MidAttribute
import io.github.crow_misia.sdp.attribute.SdpAttribute

/**
 * RFC 8866 5.14. Media Descriptions.
 * m=<media> <port> <proto> <fmt> ...
 */
data class SdpMediaDescription internal constructor(
    var type: String,
    var port: Int,
    var numberOfPorts: Int?,
    var information: SdpSessionInformation?,
    private var _protos: MutableList<String>,
    var formats: MutableList<String>,
    var connections: MutableList<SdpConnection>,
    var bandwidths: MutableList<SdpBandwidth>,
    override var attributes: MutableList<SdpAttribute>,
) : WithAttributeSdpElement, SdpElement() {
    private var cachedMid: String? = null

    var protos: List<String>
        get() = _protos
        set(value) { _protos = ArrayList(value) }

    var mid: String
        get() {
            return cachedMid ?: run {
                val mid = getAttribute<MidAttribute>()?.value
                cachedMid = mid
                mid.orEmpty()
            }
        }
        set(value) {
            if (cachedMid != value) {
                setAttribute(MidAttribute.of(value))
                cachedMid = value
            }
        }

    fun setProto(proto: String) {
        _protos = proto.splitToSequence('/').toMutableList()
    }

    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
        append(fieldPart)
        append(type)
        append(' ')
        append(port)
        numberOfPorts?.also { append('/').append(it) }
        append(' ')
        _protos.joinTo(this, "/")
        append(' ')
        formats.joinTo(this, " ")
        appendSdpLineSeparator()

        // lines of media
        information?.also { it.joinTo(this) }
        connections.forEach { it.joinTo(this) }
        bandwidths.forEach { it.joinTo(this) }
        attributes.forEach { it.joinTo(this) }
    }

    companion object {
        internal const val fieldPart = "m="

        @JvmStatic
        @JvmOverloads
        fun of(
            type: String,
            port: Int,
            numberOfPorts: Int? = null,
            protos: List<String> = emptyList(),
            formats: List<String> = emptyList(),
            information: SdpSessionInformation? = null,
            connections: List<SdpConnection> = emptyList(),
            bandwidths: List<SdpBandwidth> = emptyList(),
            attributes: List<SdpAttribute> = emptyList(),
        ): SdpMediaDescription {
            return SdpMediaDescription(
                type = type,
                port = port,
                numberOfPorts = numberOfPorts,
                information = information,
                _protos = ArrayList(protos),
                formats = ArrayList(formats),
                connections = ArrayList(connections),
                bandwidths = ArrayList(bandwidths),
                attributes = ArrayList(attributes))
        }

        internal fun parse(line: String): SdpMediaDescription {
            val values = line.substring(2).split(' ', limit = 4)
            if (values.size != 4) {
                throw SdpParseException("could not parse: $line as MediaDescription")
            }
            val tmpPort = values[1].split('/', limit = 2).map {
                it.toIntOrNull() ?: run {
                    throw SdpParseException("could not parse: $line as MediaDescription")
                }
            }
            val (port, numberOfPorts) = if (tmpPort.size > 1) {
                tmpPort[0] to tmpPort[1]
            } else {
                tmpPort[0] to null
            }
            return SdpMediaDescription(
                type = values[0],
                port = port,
                numberOfPorts = numberOfPorts,
                information = null,
                _protos = values[2].splitToSequence('/').toMutableList(),
                formats = values[3].splitToSequence(' ').toMutableList(),
                connections = arrayListOf(),
                bandwidths = arrayListOf(),
                attributes = arrayListOf()
            )
        }
    }
}
