package io.github.zncmn.sdp

import java.lang.StringBuilder

class MediaDescription @JvmOverloads constructor(
    var media: String,
    var port: Int,
    var numberOfPorts: Int? = null,
    protos: List<String> = emptyList(),
    formats: List<String> = emptyList(),
    var information: SdpSessionInformation? = null,
    connections: List<SdpConnection> = emptyList(),
    bandwidths: List<SdpBandwidth> = emptyList(),
    var key: EncryptionKey? = null,
    attributes: List<SdpAttribute> = emptyList()
) : SdpElement {
    val protos = protos.toMutableList()
    val formats = formats.toMutableList()
    val connections = connections.toMutableList()
    val bandwidths = bandwidths.toMutableList()
    val attributes = attributes.toMutableList()

    fun setProto(proto: String) {
        protos.addAll(proto.splitToSequence('/'))
    }

    fun addFormat(format: String) {
        formats.add(format)
    }

    fun addConnection(connection: SdpConnection) {
        connections.add(connection)
    }

    fun addBandwidth(bandwidth: SdpBandwidth) {
        bandwidths.add(bandwidth)
    }

    fun addAttribute(attribute: SdpAttribute) {
        attributes.add(attribute)
    }

    fun addAttribute(field: String, value: String) {
        attributes.add(BaseSdpAttribute(field, value))
    }

    fun addAttribute(field: String, value: Int) {
        attributes.add(BaseSdpAttribute(field, value))
    }

    @JvmOverloads
    fun addRTPMapAttribute(payloadType: Int, encodingName: String, clockRate: Int, encodingParameters: String? = null) {
        attributes.add(RTPMapAttribute(payloadType, encodingName, clockRate, encodingParameters))
    }

    fun getAttribute(key: String): List<SdpAttribute> {
        return attributes.filter { key.equals(it.field, true) }
    }

    fun removeAttribute(attribute: SdpAttribute) {
        attributes.remove(attribute)
    }

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("m=")
            append(media)
            append(' ')
            append(port)
            numberOfPorts?.also { append('/').append(it) }
            append(' ')
            protos.joinTo(this, "/")
            append(' ')
            formats.joinTo(this, " ")
            append("\r\n")

            // lines of media
            information?.also { it.joinTo(this) }
            connections.forEach { it.joinTo(this) }
            bandwidths.forEach { it.joinTo(this) }
            key?.also { it.joinTo(this) }
            attributes.forEach { it.joinTo(this) }
        }
    }

    companion object {
        @JvmStatic
        fun parse(line: String): MediaDescription {
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
            val description = MediaDescription(values[0], port, numberOfPorts, formats = values[3].split(' '))
            description.setProto(values[2])
            return description
        }
    }
}
