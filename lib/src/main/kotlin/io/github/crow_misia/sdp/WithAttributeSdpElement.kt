package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.SdpAttribute
import kotlin.reflect.KClass

interface WithAttributeSdpElement {
   val attributes: MutableList<SdpAttribute>

    fun <R : SdpAttribute> getAttribute(clazz: Class<R>): R? {
        return getAttributes(clazz).firstOrNull()
    }

    fun getAttributes(name: String): Sequence<SdpAttribute> {
        val field = Utils.getFieldName(name)
        return attributes.asSequence().filter { field == it.field }
    }

    fun <R : SdpAttribute> getAttributes(clazz: Class<R>): Sequence<R> {
        return attributes.asSequence().filterIsInstance(clazz)
    }

    fun addAttribute(attribute: SdpAttribute) {
        attributes.add(attribute)
    }

    fun hasAttribute(name: String): Boolean {
        return getAttributes(name).any()
    }

    fun <R : SdpAttribute> hasAttribute(clazz: Class<R>): Boolean {
        return getAttributes(clazz).any()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : SdpAttribute> setAttribute(attribute: T) {
        setAttribute(attribute, attribute::class.java as Class<T>)
    }

    fun <T : SdpAttribute> setAttribute(attribute: T, clazz: KClass<in T>) {
        setAttribute(attribute, clazz.java)
    }

    fun <T : SdpAttribute> setAttribute(attribute: T, clazz: Class<in T>) {
        val index = attributes.indexOfFirst { clazz.isInstance(it) }
        if (index < 0) {
            addAttribute(attribute)
        } else {
            attributes[index] = attribute
        }
    }

    fun removeAttribute(name: String): Boolean {
        val field = Utils.getFieldName(name)
        return attributes.removeIf { field == it.field }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: Class<R>): Boolean {
        return attributes.removeIf { clazz.isInstance(it) }
    }
}

inline fun <reified R : SdpAttribute> WithAttributeSdpElement.getAttribute(): R? {
    return getAttribute(R::class.java)
}

inline fun <reified R : SdpAttribute> WithAttributeSdpElement.getAttributes(): Sequence<R> {
    return getAttributes(R::class.java)
}

inline fun <reified R : SdpAttribute> WithAttributeSdpElement.hasAttribute(): Boolean {
    return hasAttribute(R::class.java)
}

inline fun <reified R : SdpAttribute> WithAttributeSdpElement.removeAttribute(): Boolean {
    return removeAttribute(R::class.java)
}
