@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.zncmn.sdp

import io.github.zncmn.sdp.attribute.BaseSdpAttribute
import io.github.zncmn.sdp.attribute.SdpAttribute
import kotlin.reflect.KClass

data class SdpSessionDescription internal constructor(
    var version: SdpVersion,
    var origin: SdpOrigin,
    var sessionName: SdpSessionName,
    var information: SdpSessionInformation?,
    var connection: SdpConnection?,
    var timeZones: SdpTimeZones?,
    var key: EncryptionKey?,
    val uris: MutableList<SdpUri>,
    val emails: MutableList<SdpEmail>,
    val phones: MutableList<SdpPhone>,
    val bandwidths: MutableList<SdpBandwidth>,
    val timings: MutableList<SdpTiming>,
    val attributes: MutableList<SdpAttribute>,
    val mediaDescriptions: MutableList<SdpMediaDescription>
) : SdpElement {
    fun getAttribute(name: String): SdpAttribute? {
        val field = SdpAttribute.getFieldName(name)
        return attributes.find { field == it.field }
    }

    fun <R : SdpAttribute> getAttributes(clazz: Class<R>): List<R> {
        return attributes.filterIsInstance(clazz)
    }

    fun <R : SdpAttribute> getAttributes(clazz: KClass<R>): List<R> {
        return attributes.filterIsInstance(clazz.java)
    }

    @JvmOverloads
    fun addAttribute(name: String, value: String = "") {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(name: String, value: Int) {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(name: String, value: Long) {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(attribute: SdpAttribute) {
        attributes.add(attribute)
    }

    fun hasAttribute(name: String): Boolean {
        val field = SdpAttribute.getFieldName(name)
        return attributes.find { field == it.field } != null
    }

    @JvmOverloads
    fun setAttribute(name: String, value: String = "") {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(name: String, value: Int) {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(name: String, value: Long) {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(attribute: SdpAttribute) {
        val index = attributes.indexOfFirst { attribute.field == it.field }
        if (index < 0) {
            addAttribute(attribute)
        } else {
            attributes[index] = attribute
        }
    }

    fun removeAttribute(name: String): Boolean {
        val field = SdpAttribute.getFieldName(name)
        return attributes.removeIf { field == it.field }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: Class<R>): Boolean {
        return attributes.removeIf { clazz.isInstance(it) }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: KClass<R>): Boolean {
        return attributes.removeIf { clazz.isInstance(it) }
    }

    fun addMediaScription(description: SdpMediaDescription) {
        mediaDescriptions.add(description)
    }

    fun numOfmediaScription(): Int {
        return mediaDescriptions.size
    }

    override fun toString(): String {
        return buildString { joinTo(this) }
    }

    override fun joinTo(buffer: StringBuilder) {
        buffer.apply {
            version.joinTo(this)
            origin.joinTo(this)
            sessionName.joinTo(this)
            information?.joinTo(this)
            uris.forEach { it.joinTo(this) }
            emails.forEach { it.joinTo(this) }
            phones.forEach { it.joinTo(this) }
            connection?.joinTo(this)
            bandwidths.forEach { it.joinTo(this) }
            timings.forEach { it.joinTo(this) }
            timeZones?.joinTo(this)
            key?.joinTo(this)
            attributes.forEach { it.joinTo(this) }
            mediaDescriptions.forEach { it.joinTo(this) }
        }
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun of(version: SdpVersion,
               origin: SdpOrigin,
               sessionName: SdpSessionName,
               information: SdpSessionInformation? = null,
               uris: List<SdpUri> = emptyList(),
               emails: List<SdpEmail> = emptyList(),
               phones: List<SdpPhone> = emptyList(),
               connection: SdpConnection? = null,
               bandwidths: List<SdpBandwidth> = emptyList(),
               timings: List<SdpTiming> = emptyList(),
               timeZones: SdpTimeZones? = null,
               key: EncryptionKey? = null,
               attributes: List<SdpAttribute> = emptyList(),
               mediaDescriptions: List<SdpMediaDescription> = emptyList()
        ): SdpSessionDescription {
            return SdpSessionDescription(
                version = version,
                origin = origin,
                sessionName = sessionName,
                information = information,
                connection = connection,
                timeZones = timeZones,
                key = key,
                uris = ArrayList(uris),
                emails = ArrayList(emails),
                phones = ArrayList(phones),
                bandwidths = ArrayList(bandwidths),
                timings = ArrayList(timings),
                attributes = ArrayList(attributes),
                mediaDescriptions = ArrayList(mediaDescriptions)
            )
        }

        fun parse(text: String): SdpSessionDescription {
            var version: SdpVersion? = null
            var origin: SdpOrigin? = null
            var sessionName: SdpSessionName? = null
            var information: SdpSessionInformation? = null
            val uris = arrayListOf<SdpUri>()
            val emails = arrayListOf<SdpEmail>()
            val phones = arrayListOf<SdpPhone>()
            var connection: SdpConnection? = null
            val bandwidths = arrayListOf<SdpBandwidth>()
            val timings = arrayListOf<SdpTiming>()
            var timeZones: SdpTimeZones? = null
            var key: EncryptionKey? = null
            val attributes = arrayListOf<SdpAttribute>()
            val mediaDescriptions = arrayListOf<SdpMediaDescription>()
            var lastTiming: SdpTiming? = null
            var lastMediaDescription: SdpMediaDescription? = null

            text.splitToSequence('\n')
                .map { it.trim() }
                .forEach { line ->
                    if (line.isBlank()) {
                        return@forEach
                    }
                    when (line.substring(0, 2)) {
                        "v=" -> version = SdpVersion.parse(line)
                        "o=" -> origin = SdpOrigin.parse(line)
                        "s=" -> sessionName = SdpSessionName.parse(line)
                        "i=" -> {
                            SdpSessionInformation.parse(line).also { v ->
                                lastMediaDescription?.also { it.information = v } ?: run {
                                    information = v
                                }
                            }
                        }
                        "u=" -> uris.add(SdpUri.parse(line))
                        "e=" -> emails.add(SdpEmail.parse(line))
                        "p=" -> phones.add(SdpPhone.parse(line))
                        "c=" -> {
                            SdpConnection.parse(line).also { v ->
                                lastMediaDescription?.connections?.add(v) ?: run {
                                    connection = v
                                }
                            }
                        }
                        "b=" -> {
                            val list = lastMediaDescription?.bandwidths ?: bandwidths
                            list.add(SdpBandwidth.parse(line))
                        }
                        "t=" -> {
                            SdpTiming.parse(line).also { v ->
                                lastTiming = v
                                timings.add(v)
                            }
                        }
                        "z=" -> timeZones = SdpTimeZones.parse(line)
                        "r=" -> lastTiming?.repeatTime = SdpRepeatTime.parse(line)
                        "k=" -> {
                            EncryptionKey.parse(line).also { v ->
                                lastMediaDescription?.also { it.key = v } ?: run {
                                    key = v
                                }
                            }
                        }
                        "a=" -> {
                            val list = lastMediaDescription?.attributes ?: attributes
                            list.add(Utils.parseAttribute(line))
                        }
                        "m=" -> {
                            SdpMediaDescription.parse(line).also {
                                lastMediaDescription = it
                                mediaDescriptions.add(it)
                            }
                        }
                        else -> {
                            println("Unknown Media: $line")
                        }
                    }
                }

            return of(
                version = checkNotNull(version),
                origin = checkNotNull(origin),
                sessionName = checkNotNull(sessionName),
                information = information,
                connection = connection,
                timeZones = timeZones,
                key = key,
                uris = uris,
                emails = emails,
                phones = phones,
                bandwidths = bandwidths,
                timings = timings,
                attributes = attributes,
                mediaDescriptions = mediaDescriptions
            )
        }
    }
}