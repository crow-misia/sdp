@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class RidAttribute internal constructor(
    var id: String,
    var direction: String,
    internal var _parameters: MutableMap<String, String?>
) : SdpAttribute {
    override val field = FIELD_NAME
    override val value: String
        get() = buildString { valueJoinTo(this) }
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
        _parameters[key.trim()] = value.toString()
    }

    fun removeParameter(key: String) {
        _parameters.remove(key.trim())
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
            append(id)
            append(' ')
            append(direction)
            _parameters.entries.forEachIndexed { index, entry ->
                if (index == 0) {
                    append(' ')
                } else {
                    append("; ")
                }
                append(entry.key)
                entry.value?.also {
                    append('=')
                    append(it)
                }
            }
        }
    }

    companion object {
        internal const val FIELD_NAME = "rid"

        @JvmStatic @JvmOverloads
        fun of(id: String, direction: String, parameters: String? = null): RidAttribute {
            return RidAttribute(id, direction, linkedMapOf()).also {
                it.setParameter(parameters)
            }
        }

        internal fun parse(value: String): RidAttribute {
            val values = value.split(' ', limit = 3)
            val size = values.size
            if (size < 2) {
                throw SdpParseException("could not parse: $value as RidAttribute")
            }
            return of(
                id = values[0],
                direction = values[1],
                parameters = if (size > 2) values[2] else null
            )
        }
    }
}