@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.crow_misia.sdp.attribute

abstract class WithParametersAttribute internal constructor(
    internal open var _parameters: MutableMap<String, Any?> = linkedMapOf()
) : SdpAttribute {
    companion object {
        private val wellKnownParameters = mapOf(
            // H264 codec parameters.
            "profile-level-id" to "s",
            "packetization-mode" to "d",
            // VP9 codec parameters
            "profile-id" to "s"
        )
    }

    val isNotEmptyParameters: Boolean
        get() = _parameters.isNotEmpty()

    var parameters: Map<String, Any?>
        get() = _parameters
        set(value) { _parameters = LinkedHashMap(value) }

    fun setParameter(parameters: String?) {
        if (parameters.isNullOrBlank()) {
            return
        }
        parameters.splitToSequence(';').forEach { parameter ->
            val values = parameter.split('=')
            val size = values.size
            check(size <= 2)
            val key = values[0].trim()
            _parameters[key] = if (size > 1) normalizeParam(key, values[1]) else null
        }
    }

    @JvmOverloads
    fun addParameter(key: String, value: String? = null) {
        _parameters[key.trim()] = value
    }

    fun addParameter(key: String, value: Int?) {
        _parameters[key.trim()] = value
    }

    fun removeParameter(key: String) {
        _parameters.remove(key.trim())
    }

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

   protected open fun valueJoinTo(buffer: StringBuilder) {
        buffer.apply {
            _parameters.entries.forEachIndexed { index, entry ->
                if (index == 0) {
                    append(' ')
                } else {
                    append(';')
                }
                append(entry.key)
                entry.value?.also {
                    append('=')
                    append(it)
                }
            }
        }
    }

    private fun normalizeParam(key: String, str: String): Any? {
        return when (wellKnownParameters[key]) {
            "s" -> str
            "d" -> str.toIntOrNull()
            else -> {
                str.toIntOrNull() ?: str.toFloatOrNull() ?: str
            }
        }
    }
}