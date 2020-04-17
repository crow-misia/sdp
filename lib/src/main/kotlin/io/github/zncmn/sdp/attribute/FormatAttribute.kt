@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.SdpParseException

data class FormatAttribute internal constructor(
    var format: Int,
    internal var _parameters: MutableMap<String, String?>
) : SdpAttribute {
    override val field = FIELD_NAME
    override val value: String
        get() = buildString { valueJoinTo(this) }

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
            append(format)
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
        internal const val FIELD_NAME = "fmtp"

        @JvmStatic @JvmOverloads
        fun of(format: Int, parameters: String? = null): FormatAttribute {
            return FormatAttribute(format, linkedMapOf()).also {
                it.setParameter(parameters)
            }
        }

        internal fun parse(value: String): FormatAttribute {
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 1) {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            val format = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            return of(
                format = format,
                parameters = if (size > 1) values[1] else null
            )
        }
    }
}