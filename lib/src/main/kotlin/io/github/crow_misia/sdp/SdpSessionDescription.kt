package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.SdpAttribute

data class SdpSessionDescription internal constructor(
    var version: SdpVersion,
    var origin: SdpOrigin,
    var sessionName: SdpSessionName,
    var information: SdpSessionInformation?,
    var connection: SdpConnection?,
    var timeZones: SdpTimeZones?,
    var key: EncryptionKey?,
    var uris: MutableList<SdpUri>,
    var emails: MutableList<SdpEmail>,
    var phones: MutableList<SdpPhone>,
    var bandwidths: MutableList<SdpBandwidth>,
    var timings: MutableList<SdpTiming>,
    override val attributes: MutableList<SdpAttribute>,
    private val mediaDescriptions: MutableList<SdpMediaDescription>
) : WithAttributeSdpElement, SdpElement() {
    private val midToIndex = hashMapOf<String, Int>().also { map ->
        mediaDescriptions.forEachIndexed { idx, desc -> map[desc.mid] = idx }
    }

    fun getMediaDescriptions(): Sequence<SdpMediaDescription> = mediaDescriptions.asSequence()

    fun getMediaDescriptionAt(index: Int): SdpMediaDescription {
        return mediaDescriptions[index]
    }

    fun getMediaDescription(mid: String): SdpMediaDescription? {
        val index = midToIndex[mid] ?: -1
        val size = mediaDescriptions.size
        return if (index < 0 || index >= size) null else mediaDescriptions[index]
    }

    fun addMediaDescription(description: SdpMediaDescription) {
        val size = mediaDescriptions.size
        midToIndex[description.mid] = size
        mediaDescriptions.add(description)
    }

    fun setMediaDescription(description: SdpMediaDescription, mid: String) {
        val index = midToIndex.remove(mid) ?: -1
        val size = mediaDescriptions.size
        if (index < 0 || index >= size) {
            midToIndex[description.mid] = size
            mediaDescriptions.add(description)
        } else {
            midToIndex[description.mid] = index
            mediaDescriptions[index] = description
        }
    }

    fun numOfMediaDescription(): Int {
        return mediaDescriptions.size
    }

    override fun toString() = super.toString()

    override fun joinTo(buffer: StringBuilder) = buffer.apply {
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

    companion object {
        @JvmStatic
        @JvmOverloads
        fun of(
            version: SdpVersion,
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
            mediaDescriptions: List<SdpMediaDescription> = emptyList(),
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
                        SdpVersion.fieldPart -> version = SdpVersion.parse(line)
                        SdpOrigin.fieldPart -> origin = SdpOrigin.parse(line)
                        SdpSessionName.fieldPart -> sessionName = SdpSessionName.parse(line)
                        SdpSessionInformation.fieldPart -> {
                            SdpSessionInformation.parse(line).also { v ->
                                lastMediaDescription?.also { it.information = v } ?: run {
                                    information = v
                                }
                            }
                        }
                        SdpUri.fieldPart -> uris.add(SdpUri.parse(line))
                        SdpEmail.fieldPart -> emails.add(SdpEmail.parse(line))
                        SdpPhone.fieldPart -> phones.add(SdpPhone.parse(line))
                        SdpConnection.fieldPart -> {
                            SdpConnection.parse(line).also { v ->
                                lastMediaDescription?.connections?.add(v) ?: run {
                                    connection = v
                                }
                            }
                        }
                        SdpBandwidth.fieldPart -> {
                            val list = lastMediaDescription?.bandwidths ?: bandwidths
                            list.add(SdpBandwidth.parse(line))
                        }
                        SdpTiming.fieldPart -> {
                            SdpTiming.parse(line).also { v ->
                                lastTiming = v
                                timings.add(v)
                            }
                        }
                        SdpTimeZones.fieldPart -> timeZones = SdpTimeZones.parse(line)
                        SdpRepeatTime.fieldPart -> lastTiming?.repeatTime = SdpRepeatTime.parse(line)
                        EncryptionKey.fieldPart -> {
                            EncryptionKey.parse(line).also { v ->
                                lastMediaDescription?.also { it.key = v } ?: run {
                                    key = v
                                }
                            }
                        }
                        SdpAttribute.fieldPart -> {
                            val list = lastMediaDescription?.attributes ?: attributes
                            list.add(Utils.parseAttribute(line))
                        }
                        SdpMediaDescription.fieldPart -> {
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
