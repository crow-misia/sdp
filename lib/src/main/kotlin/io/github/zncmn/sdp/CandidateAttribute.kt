package io.github.zncmn.sdp

import java.lang.StringBuilder

class CandidateAttribute @JvmOverloads constructor(
    var fundatioon: String,
    var componentId: Int,
    var transport: String,
    var priority: Int,
    var address: String,
    var port: Int,
    var type: String,
    var relAddr: String? = null,
    var relPort: String? = null,
    extensions: List<Extension> = emptyList()
) : SdpAttribute {
    val extensions = extensions.toMutableList()

    override val field = "candidate"
    override val value: String?
        get() = buildString { valueJoinTo(this) }

   fun addExtension(extension: Extension) {
        extensions.add(extension)
    }

    fun addExtension(name: String, value: String) {
        extensions.add(Extension(name, value))
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
            append(fundatioon)
            append(' ')
            append(componentId)
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
            relAddr?.also {
                append(" raddr ")
                append(it)
            }
            relPort?.also {
                append(" rport ")
                append(it)
            }
            extensions.forEach {
                append(' ')
                append(it.name)
                append(' ')
                append(it.value)
            }
        }
    }

    companion object {
        @JvmStatic
        fun parse(value: String?): CandidateAttribute {
            value ?: run {
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            }
            val values = value.split(' ')
            val size = values.size
            if (size < 8) {
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            }
            val componentId = values[1].toIntOrNull()
            val priority = values[3].toIntOrNull()
            val port = values[5].toIntOrNull()
            if (componentId == null || priority == null || port == null) {
                throw SdpParseException("could not parse: $value as CandidateAttribute")
            }

            var type: String? = null
            var relAddr: String? = null
            var relPort: String? = null
            val extensions = arrayListOf<Extension>()
            (6 until size step 2).map { index ->
                val n = values[index]
                val v = values[index + 1]
                when (n) {
                    "typ" -> type = v
                    "raddr" -> relAddr = v
                    "rport" -> relPort = v
                    else -> extensions.add(Extension(n, v))
                }
            }

            return CandidateAttribute(values[0], componentId, values[2], priority, values[4], port,
                type ?: run {
                    throw SdpParseException("could not parse: $value as CandidateAttribute")
                }, relAddr, relPort, extensions)
        }
    }

    data class Extension(val name: String, val value: String)
}