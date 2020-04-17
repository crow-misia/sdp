@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp

data class CandidateAttribute internal constructor(
    var foundation: String,
    var component: Int,
    var transport: String,
    var priority: Int,
    var address: String,
    var port: Int,
    var type: String,
    internal var _extensions: MutableList<Extension>
) : SdpAttribute {
    override val field = FIELD_NAME
    override val value: String?
        get() = buildString { valueJoinTo(this) }

    var extensions: List<Extension>
        get() = _extensions
        set(value) { _extensions = ArrayList(value) }

    fun addExtension(name: String, value: Int) {
        addExtension(Extension(name, value.toString()))
    }

    fun addExtension(name: String, value: String) {
        addExtension(Extension(name, value))
    }

    fun addExtension(extension: Extension) {
        _extensions.add(extension)
    }

    fun hasExtension(name: String): Boolean {
        return _extensions.find { name.equals(it.name, ignoreCase = true) } != null
    }

    fun setExtension(name: String, value: String) {
        setExtension(Extension(name, value))
    }

    fun setExtension(extension: Extension) {
        val index =  _extensions.indexOfFirst { extension.name.equals(it.name, ignoreCase = true) }
        if (index < 0) {
            addExtension(extension)
        } else {
            _extensions[index] = extension
        }
    }

    fun removeExtension(name: String): Boolean {
        return _extensions.removeIf { name.equals(it.name, ignoreCase = true) }
    }

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            append("a=")
            append(field)
            append(':')
            valueJoinTo(this)
            append("\r\n")
        }
    }

    private fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
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
                append(it.name)
                append(' ')
                append(it.value)
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "candidate"

        @JvmStatic @JvmOverloads
        fun of(foundation: String,
               component: Int,
               transport: String,
               priority: Int,
               ip: String,
               port: Int,
               type: String,
               extensions: List<Extension> = emptyList()
        ): CandidateAttribute {
            return CandidateAttribute(
                foundation, component, transport, priority,
                ip, port, type, ArrayList(extensions))
        }

        internal fun parse(value: String?): CandidateAttribute {
            value ?: run {
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            }
            val values = value.split(' ')
            val size = values.size
            if (size < 6) {
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            }
            val componentId = values[1].toIntOrNull()
            val priority = values[3].toIntOrNull()
            val port = values[5].toIntOrNull()
            if (componentId == null || priority == null || port == null) {
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            }

            var type: String? = null
            val extensions = arrayListOf<Extension>()
            (6 until size step 2).map { index ->
                val n = values[index]
                val v = values[index + 1]
                when (n) {
                    "typ" -> type = v
                    else -> extensions.add(Extension(n, v))
                }
            }

            return CandidateAttribute(values[0], componentId, values[2], priority, values[4], port,
                type ?: run {
                    throw SdpParseException("could not parse: $value as CandidateAttribute")
                }, extensions)
        }
    }

    data class Extension internal constructor(val name: String, val value: String) {
        companion object {
            @JvmStatic
            fun of(name: String, value: String): Extension {
                return Extension(name.toLowerCase(), value)
            }
        }
    }
}