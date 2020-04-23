@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp.attribute

abstract class WithParametersAttribute internal constructor(
    internal open var _parameters: MutableMap<String, String?> = linkedMapOf()
) : SdpAttribute {
    val isNotEmptyParameters: Boolean
        get() = _parameters.isNotEmpty()

    var parameters: Map<String, String?>
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
            _parameters[values[0].trim()] = if (size > 1) values[1] else null
        }
    }

    @JvmOverloads
    fun addParameter(key: String, value: String? = null) {
        _parameters[key.trim()] = value
    }

    fun addParameter(key: String, value: Int?) {
        _parameters[key.trim()] = value?.toString()
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
}