@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp

import io.github.zncmn.sdp.attribute.BaseSdpAttribute
import io.github.zncmn.sdp.attribute.MidAttribute
import io.github.zncmn.sdp.attribute.SdpAttribute
import kotlin.reflect.KClass

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
    val attributes: MutableList<SdpAttribute>
) : SdpElement {
    val protos: List<String>
        get() = _protos

    val mid: String
        get() = getAttribute(MidAttribute.FIELD_NAME)?.value.orEmpty()

    fun setProto(proto: String) {
        _protos = proto.splitToSequence('/').toMutableList()
    }

    fun getAttribute(name: String): SdpAttribute? {
        val field = SdpAttribute.getFieldName(name)
        return attributes.find { field == it.field }
    }

    fun <R : SdpAttribute> getAttributes(clazz: Class<R>): List<R> {
        return attributes.filterIsInstance(clazz)
    }

    fun <R : SdpAttribute> getAttributes(clazz: KClass<R>): List<R> {
        return attributes.filterIsInstance(clazz.java)
    }

    @JvmOverloads
    fun addAttribute(name: String, value: String = "") {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(name: String, value: Int) {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(attribute: SdpAttribute) {
        attributes.add(attribute)
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
        val field = SdpAttribute.getFieldName(attribute.field)
        val index = attributes.indexOfFirst { field == it.field }
        if (index < 0) {
            addAttribute(attribute)
        } else {
            attributes[index] = attribute
        }
    }

    fun removeAttribute(name: String): Boolean {
        val field = SdpAttribute.getFieldName(name)
        return attributes.removeIf { field == it.field }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: Class<R>): Boolean {
        return attributes.removeIf { clazz.isInstance(it) }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: KClass<R>): Boolean {
        return attributes.removeIf { clazz.isInstance(it) }
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
