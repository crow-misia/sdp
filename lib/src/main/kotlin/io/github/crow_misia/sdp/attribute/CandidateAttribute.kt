@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils

data class CandidateAttribute internal constructor(
    var foundation: String,
    var component: Long,
    var transport: String,
    var priority: Long,
    var address: String,
    var port: Int,
    var type: String,
    internal var _extensions: MutableMap<String, String>
) : SdpAttribute() {
    override val field = fieldName

    var extensions: Map<String, String>
        get() = _extensions
        set(value) {
            _extensions = LinkedHashMap<String, String>().also {
                value.forEach { (k, v) -> it[k] = v }
            }
        }

    fun addExtension(name: String, value: Int) {
        addExtension(name, value.toString())
    }

    fun addExtension(name: String, value: Long) {
        addExtension(name, value.toString())
    }

    fun addExtension(name: String, value: String) {
        _extensions[Utils.getName(name)] = value
   }

    fun hasExtension(name: String): Boolean {
        return _extensions.containsKey(Utils.getName(name))
    }

    fun setExtension(name: String, value: Int) {
        setExtension(name, value.toString())
    }

    fun setExtension(name: String, value: Long) {
        setExtension(name, value.toString())
    }

    fun setExtension(name: String, value: String) {
        _extensions[Utils.getName(name)] = value
    }

    fun removeExtension(name: String): Boolean {
        return _extensions.remove(Utils.getName(name)) != null
    }

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(foundation)
        append(' ')
        append(component)
        append(' ')
        append(transport)
        append(' ')
        append(priority)
        append(' ')
        append(address)
        append(' ')
        append(port)
        append(" typ ")
        append(type)
        _extensions.forEach {
            append(' ')
            append(it.key)
            append(' ')
            append(it.value)
        }
    }

    companion object {
        internal const val fieldName = "candidate"

        @JvmStatic @JvmOverloads
        fun of(foundation: String,
               component: Long,
               transport: String,
               priority: Long,
               address: String,
               port: Int,
               type: String,
               extensions: Map<String, String> = emptyMap()
        ): CandidateAttribute {
            return CandidateAttribute(
                foundation = foundation,
                component = component,
                transport = transport,
                priority = priority,
                address = address,
                port = port,
                type = type,
                _extensions = LinkedHashMap(extensions)
            )
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ')
            val size = values.size
            if (size < 6) {
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            }
            val component = values[1].toLongOrNull()
            val priority = values[3].toLongOrNull()
            val port = values[5].toIntOrNull()
            if (component == null || priority == null || port == null) {
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            }

            var type: String? = null
            val extensions = linkedMapOf<String, String>()
            (6 until size step 2).map { index ->
                val n = values[index]
                val v = values[index + 1]
                when (n) {
                    "typ" -> type = v
                    else -> extensions[Utils.getName(n)] = v
                }
            }

            return CandidateAttribute(
                foundation = values[0],
                component = component,
                transport = values[2],
                priority = priority,
                address = values[4],
                port = port,
                type = type ?: run {
                    throw SdpParseException("could not parse: $value as CandidateAttribute")
                },
                _extensions = extensions
            )
        }
    }
}
