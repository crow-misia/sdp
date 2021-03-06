package io.github.zncmn.sdp.attribute

import io.github.zncmn.sdp.Utils
import java.util.*

data class SetupAttribute internal constructor(
    override var value: String
) : BaseSdpAttribute(FIELD_NAME, value) {
    companion object {
        internal const val FIELD_NAME = "setup"

        private val MAPPING by lazy {
            EnumMap<Type, SetupAttribute>(Type::class.java).also {
                Type.values().forEach { v -> it[v] = SetupAttribute(v.value) }
            }
        }

        @JvmStatic
        fun of(type: Type): SetupAttribute {
            return requireNotNull(MAPPING[type])
        }

        @JvmStatic
        fun of(type: String): SetupAttribute {
            return SetupAttribute(Utils.getName(type))
        }

        internal fun parse(value: String): SdpAttribute {
            return of(value)
        }
    }

    enum class Type {
        ACTPASS,
        ACTIVE,
        PASSIVE
        ;
        internal val value = Utils.getName(name)
    }
}
