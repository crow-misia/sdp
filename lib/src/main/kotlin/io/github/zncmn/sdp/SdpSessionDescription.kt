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
    private var _uris: MutableList<SdpUri>,
    private var _emails: MutableList<SdpEmail>,
    private var _phones: MutableList<SdpPhone>,
    private var _bandwidths: MutableList<SdpBandwidth>,
    private var _timings: MutableList<SdpTiming>,
    private var _attributes: MutableList<SdpAttribute>,
    private var _mediaDescriptions: MutableList<SdpMediaDescription>
) : SdpElement {
    var urls: List<SdpUri>
        get() = _uris
        set(value) { _uris = ArrayList(value) }

    var emails: List<SdpEmail>
        get() = _emails
        set(value) { _emails = ArrayList(value) }

    var phones: List<SdpPhone>
        get() = _phones
        set(value) { _phones = ArrayList(value) }

    var bandwidths: List<SdpBandwidth>
        get() = _bandwidths
        set(value) { _bandwidths = ArrayList(value) }

    var timing: List<SdpTiming>
        get() = _timings
        set(value) { _timings = ArrayList(value) }

    var attributes: List<SdpAttribute>
        get() = _attributes
        set(value) { _attributes = ArrayList(value) }

    var mediaDescriptions: List<SdpMediaDescription>
        get() = _mediaDescriptions
        set(value) { _mediaDescriptions = ArrayList(value) }

    fun getAttribute(name: String): SdpAttribute? {
        return _attributes.find { name.equals(it.field, ignoreCase = true) }
    }

    fun <R : SdpAttribute> getAttributes(clazz: Class<R>): List<R> {
        return _attributes.filterIsInstance(clazz)
    }

    fun <R : SdpAttribute> getAttributes(clazz: KClass<R>): List<R> {
        return _attributes.filterIsInstance(clazz.java)
    }

    @JvmOverloads
    fun addAttribute(name: String, value: String = "") {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(name: String, value: Int) {
        addAttribute(BaseSdpAttribute.of(name, value))
    }

    fun addAttribute(attribute: SdpAttribute) {
        _attributes.add(attribute)
    }

    fun hasAttribute(name: String): Boolean {
        return _attributes.find { name.equals(it.field, ignoreCase = true) } != null
    }

    @JvmOverloads
    fun setAttribute(name: String, value: String = "") {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(name: String, value: Int) {
        setAttribute(BaseSdpAttribute.of(name, value))
    }

    fun setAttribute(attribute: SdpAttribute) {
        val index = _attributes.indexOfFirst { attribute.field.equals(it.field, ignoreCase = true) }
        if (index < 0) {
            addAttribute(attribute)
        } else {
            _attributes[index] = attribute
        }
    }

    fun removeAttribute(name: String): Boolean {
        return _attributes.removeIf { name.equals(it.field, ignoreCase = true) }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: Class<R>): Boolean {
        return _attributes.removeIf { clazz.isInstance(it) }
    }

    fun <R : SdpAttribute> removeAttribute(clazz: KClass<R>): Boolean {
        return _attributes.removeIf { clazz.isInstance(it) }
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
            _uris.forEach { it.joinTo(this) }
            _emails.forEach { it.joinTo(this) }
            _phones.forEach { it.joinTo(this) }
            connection?.joinTo(this)
            _bandwidths.forEach { it.joinTo(this) }
            _timings.forEach { it.joinTo(this) }
            timeZones?.joinTo(this)
            key?.joinTo(this)
            _attributes.forEach { it.joinTo(this) }
            _mediaDescriptions.forEach { it.joinTo(this) }
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
                _uris = ArrayList(uris),
                _emails = ArrayList(emails),
                _phones = ArrayList(phones),
                _bandwidths = ArrayList(bandwidths),
                _timings = ArrayList(timings),
                _attributes = ArrayList(attributes),
                _mediaDescriptions = ArrayList(mediaDescriptions)
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
                                lastMediaDescription?._connections?.add(v) ?: run {
                                    connection = v
                                }
                            }
                        }
                        "b=" -> {
                            val list = lastMediaDescription?._bandwidths ?: bandwidths
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
                            val list = lastMediaDescription?._attributes ?: attributes
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

            return SdpSessionDescription(
                version = checkNotNull(version),
                origin = checkNotNull(origin),
                sessionName = checkNotNull(sessionName),
                information = information,
                connection = connection,
                timeZones = timeZones,
                key = key,
                _uris = uris,
                _emails = emails,
                _phones = phones,
                _bandwidths = bandwidths,
                _timings = timings,
                _attributes = attributes,
                _mediaDescriptions = mediaDescriptions
            )
        }
    }
}