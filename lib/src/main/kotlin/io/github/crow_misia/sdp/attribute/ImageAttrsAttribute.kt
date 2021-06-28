package io.github.crow_misia.sdp.attribute

import io.github.crow_misia.sdp.SdpParseException
import io.github.crow_misia.sdp.Utils
import java.util.*
import kotlin.math.max

data class ImageAttrsAttribute internal constructor(
    var pt: String,
    var dir1: String,
    var attrs1: String,
    var dir2: String?,
    var attrs2: String?,
) : SdpAttribute() {
    override val field = fieldName

    override fun toString() = super.toString()

    override fun valueJoinTo(buffer: StringBuilder) = buffer.apply {
        append(':')
        append(pt)
        append(' ')
        append(dir1)
        append(' ')
        append(attrs1)
        if (!dir2.isNullOrBlank()) {
            append(' ')
            append(dir2)
            append(' ')
            append(attrs2)
        }
    }

    companion object {
        internal const val fieldName = "imageattr"

        @JvmStatic
        @JvmOverloads
        fun of(
            pt: String,
            dir1: String,
            attrs1: String,
            dir2: String? = null,
            attrs2: String? = null,
        ): ImageAttrsAttribute {
            return ImageAttrsAttribute(pt, Utils.getName(dir1), attrs1, dir2?.lowercase(Locale.ENGLISH), attrs2)
        }

        internal fun parse(value: String): SdpAttribute {
            val values = value.split(' ')
            val size = values.size
            if (size < 3) {
                throw SdpParseException("could not parse: $value as ImageAttrsAttribute")
            }

            val dir2Index = max(values.subList(3, size).indexOfFirst {
                val dir = it.trim().lowercase(Locale.ENGLISH)
                return@indexOfFirst dir == "send" || dir == "recv"
            }, 0) + 3

            val dir1 = values[1]
            val attrs1 = values.subList(2, if (dir2Index < size) dir2Index else size).joinToString(" ")
            val (dir2, attrs2) = if (dir2Index + 1 < size) {
                values[dir2Index] to values.subList(dir2Index + 1, size).joinToString(" ")
            } else {
                null to null
            }

            return of(
                pt = values[0],
                dir1 = dir1,
                attrs1 = attrs1,
                dir2 = dir2,
                attrs2 = attrs2
            )
        }
    }
}
