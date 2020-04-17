@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp

import io.github.zncmn.sdp.attribute.BaseSdpAttribute
import io.github.zncmn.sdp.attribute.RTPMapAttribute
import io.github.zncmn.sdp.attribute.SdpAttribute
import kotlin.reflect.KClass

data class SdpMediaDescription internal constructor(
    var type: String,
    var port: Int,
    var numberOfPorts: Int?,
    var information: SdpSessionInformation?,
    var key: EncryptionKey?,
    internal var _protos: MutableList<String>,
    internal var _formats: MutableList<String>,
    internal var _connections: MutableList<SdpConnection>,
    internal var _bandwidths: MutableList<SdpBandwidth>,
    internal var _attributes: MutableList<SdpAttribute>
) : SdpElement {
    var protos: List<String>
        get() = _protos
        set(value) { _protos = ArrayList(value) }

    var formats: List<String>
        get() = _formats
        set(value) { _formats = ArrayList(value) }

    var connections: List<SdpConnection>
        get() = _connections
        set(value) { _connections = ArrayList(value) }

    var bandwidths: List<SdpBandwidth>
        get() = _bandwidths
        set(value) { _bandwidths = ArrayList(value) }

    var attributes: List<SdpAttribute>
        get() = _attributes
        set(value) { _attributes = ArrayList(value) }

    fun setProto(proto: String) {
        _protos.addAll(proto.splitToSequence('/'))
    }

    fun addFormat(format: String) {
        _formats.add(format)
    }

    fun addConnection(connection: SdpConnection) {
        _connections.add(connection)
    }

    fun addBandwidth(bandwidth: SdpBandwidth) {
        _bandwidths.add(bandwidth)
    }

    fun getAttribute(name: String): SdpAttribute? {
        val lowerName = name.toLowerCase()
        return _attributes.find { lowerName == it.field }
    }

    fun <R : SdpAttribute> getAttributes(clazz: Class<R>): List<R> {
        return _attributes.filterIsInstance(clazz)
    }

    fun <R : SdpAttribute> getAttributes(clazz: KClass<R>): List<R> {
        return _attributes.filterIsInstance(clazz.java)
    }

    @JvmOverloads
    fun addAttribute(name: String, value: String = "") {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(name: String, value: Int) {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(attribute: SdpAttribute) {
        _attributes.add(attribute)
    }

    fun hasAttribute(name: String): Boolean {
        return getAttribute(name) != null
    }

    @JvmOverloads
    fun setAttribute(name: String, value: String = "") {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(name: String, value: Int) {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(attribute: SdpAttribute) {
        val lowerName = attribute.field.toLowerCase()
        val index = _attributes.indexOfFirst { lowerName == it.field }
        if (index < 0) {
            addAttribute(attribute)
        } else {
            _attributes[index] = attribute
        }
    }

    fun removeAttribute(name: String): Boolean {
        val lowerName = name.toLowerCase()
        return _attributes.removeIf { lowerName == it.field }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: Class<R>): Boolean {
        return _attributes.removeIf { clazz.isInstance(it) }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: KClass<R>): Boolean {
        return _attributes.removeIf { clazz.isInstance(it) }
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
            _formats.joinTo(this, " ")
            append("\r\n")

            // lines of media
            information?.also { it.joinTo(this) }
            _connections.forEach { it.joinTo(this) }
            _bandwidths.forEach { it.joinTo(this) }
            key?.also { it.joinTo(this) }
            _attributes.forEach { it.joinTo(this) }
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
                _formats = ArrayList(formats),
                _connections = ArrayList(connections),
                _bandwidths = ArrayList(bandwidths),
                _attributes = ArrayList(attributes))
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
                _formats = values[3].splitToSequence(' ').toMutableList(),
                _connections = arrayListOf(),
                _bandwidths = arrayListOf(),
                _attributes = arrayListOf())
        }
    }
}
