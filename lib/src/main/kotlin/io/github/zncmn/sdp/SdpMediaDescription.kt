@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp

data class SdpMediaDescription internal constructor(
    var media: String,
    var port: Int,
    var numberOfPorts: Int?,
    val protos: MutableList<String>,
    val formats: MutableList<String>,
    var information: SdpSessionInformation?,
    val connections: MutableList<SdpConnection>,
    val bandwidths: MutableList<SdpBandwidth>,
    var key: EncryptionKey?,
    val attributes: MutableList<SdpAttribute>
) : SdpElement {
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

    @JvmOverloads
    fun addRTPMapAttribute(payloadType: Int, encodingName: String, clockRate: Int, encodingParameters: String? = null) {
        attributes.add(RTPMapAttribute.of(payloadType, encodingName, clockRate, encodingParameters))
    }

    @JvmOverloads
    fun addAttribute(name: String, value: String? = null) {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(name: String, value: Int) {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(attribute: SdpAttribute) {
        attributes.add(attribute)
    }

    fun hasAttribute(name: String): Boolean {
        return attributes.find { name.equals(it.field, ignoreCase = true) } != null
    }

    @JvmOverloads
    fun setAttribute(name: String, value: String? = null) {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(name: String, value: Int) {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(attribute: SdpAttribute) {
        val index = attributes.indexOfFirst { attribute.field.equals(it.field, ignoreCase = true) }
        if (index < 0) {
            addAttribute(attribute)
        } else {
            attributes[index] = attribute
        }
    }

    fun removeAttribute(name: String): Boolean {
        return attributes.removeIf { name.equals(it.field, ignoreCase = true) }
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
        @JvmStatic @JvmOverloads
        fun of(media: String,
               port: Int,
               numberOfPorts: Int? = null,
               protos: List<String> = emptyList(),
               formats: List<String> = emptyList(),
               information: SdpSessionInformation? = null,
               connections: List<SdpConnection> = emptyList(),
               bandwidths: List<SdpBandwidth> = emptyList(),
               key: EncryptionKey? = null,
               attributes: List<SdpAttribute> = emptyList()
        ): SdpMediaDescription {
            return SdpMediaDescription(
                media, port, numberOfPorts,
                ArrayList(protos), ArrayList(formats), information,
                ArrayList(connections), ArrayList(bandwidths), key,
                ArrayList(attributes))
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
            val description = of(values[0], port, numberOfPorts, formats = values[3].split(' '))
            description.setProto(values[2])
            return description
        }
    }
}
