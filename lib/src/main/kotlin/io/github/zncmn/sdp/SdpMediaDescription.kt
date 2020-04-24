@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp

import io.github.zncmn.sdp.attribute.MidAttribute
import io.github.zncmn.sdp.attribute.SdpAttribute

data class SdpMediaDescription internal constructor(
    var type: String,
    var port: Int,
    var numberOfPorts: Int?,
    var information: SdpSessionInformation?,
    var key: EncryptionKey?,
    private var _protos: MutableList<String>,
    val formats: MutableList<String>,
    val connections: MutableList<SdpConnection>,
    val bandwidths: MutableList<SdpBandwidth>,
    override val attributes: MutableList<SdpAttribute>
) : WithAttributeSdpElement, SdpElement {
    private var cachedMid: String? = null

    var protos: List<String>
        get() = _protos
        set(value) { _protos = ArrayList(value) }

    var mid: String
        get() {
            return cachedMid ?: run {
                val mid = getAttribute(MidAttribute::class)?.value.orEmpty()
                cachedMid = mid
                mid
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

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("m=")
            append(type)
            append(' ')
            append(port)
            numberOfPorts?.also { append('/').append(it) }
            append(' ')
            _protos.joinTo(this, "/")
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
        fun of(type: String,
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
                type = type,
                port = port,
                numberOfPorts = numberOfPorts,
                information = information,
                key = key,
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
                key = null,
                _protos = values[2].splitToSequence('/').toMutableList(),
                formats = values[3].splitToSequence(' ').toMutableList(),
                connections = arrayListOf(),
                bandwidths = arrayListOf(),
                attributes = arrayListOf())
        }
    }
}
