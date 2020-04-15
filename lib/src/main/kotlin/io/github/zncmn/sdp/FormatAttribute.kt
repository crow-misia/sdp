package io.github.zncmn.sdp

import java.lang.StringBuilder

class FormatAttribute @JvmOverloads constructor(
    var format: Int,
    parameters: String? = null
) : SdpAttribute {
    val parameters = mutableMapOf<String, String?>()

    override val field = "fmtp"
    override val value: String?
        get() = buildString { valueJoinTo(this) }

    init {
        setParameter(parameters)
    }

    fun setParameter(parameters: String?) {
        if (parameters.isNullOrBlank()) {
            return
        }
        parameters.splitToSequence(';').forEach { parameter ->
            val values = parameter.split('=')
            val size = values.size
            check(size <= 2)
            this.parameters[values[0].trim()] = if (size > 1) values[1] else null
        }
    }

    @JvmOverloads
    fun addParameter(key: String, value: String? = null) {
        parameters[key.trim()] = value
    }

    fun addParameter(key: String, value: Int?) {
        parameters[key.trim()] = value.toString()
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
            parameters.entries.forEachIndexed { index, entry ->
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
        @JvmStatic
        fun parse(value: String?): FormatAttribute {
            value ?: run {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            val values = value.split(' ', limit = 2)
            val size = values.size
            if (size < 1) {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            val format = values[0].toIntOrNull() ?: run {
                throw SdpParseException("could not parse: $value as FormatAttribute")
            }
            return FormatAttribute(format, if (size > 1) values[1] else "")

        }
    }
}