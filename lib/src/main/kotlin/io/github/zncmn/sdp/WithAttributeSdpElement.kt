package io.github.zncmn.sdp

import io.github.zncmn.sdp.attribute.SdpAttribute
import kotlin.reflect.KClass

interface WithAttributeSdpElement {
   val attributes: MutableList<SdpAttribute>

    fun <R : SdpAttribute> getAttribute(clazz: Class<R>): R? {
        return attributes.asSequence().filterIsInstance(clazz).firstOrNull()
    }

    fun <R : SdpAttribute> getAttribute(clazz: KClass<R>): R? {
        return getAttribute(clazz.java)
    }

    fun getAttributes(name: String): Sequence<SdpAttribute> {
        val field = Utils.getFieldName(name)
        return attributes.asSequence().filter { field == it.field }
    }

    fun <R : SdpAttribute> getAttributes(clazz: Class<R>): Sequence<R> {
        return attributes.asSequence().filterIsInstance(clazz)
    }

    fun <R : SdpAttribute> getAttributes(clazz: KClass<R>): Sequence<R> {
        return getAttributes(clazz.java)
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

    fun <R : SdpAttribute> hasAttribute(clazz: KClass<R>): Boolean {
        return hasAttribute(clazz.java)
    }

    fun setAttribute(attribute: SdpAttribute) {
        val index = attributes.asSequence().indexOfFirst { attribute.field == it.field }
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

    fun <R : SdpAttribute> removeAttribute(clazz: KClass<R>): Boolean {
        return removeAttribute(clazz.java)
    }
}
