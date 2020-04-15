package io.github.zncmn.sdp

import java.lang.StringBuilder

class SessionDescription @JvmOverloads constructor(
    var version: SdpVersion,
    var origin: SdpOrigin,
    var sessionName: SdpSessionName,
    var information: SdpSessionInformation? = null,
    uris: List<SdpUri> = emptyList(),
    emails: List<SdpEmail> = emptyList(),
    phones: List<SdpPhone> = emptyList(),
    var connection: SdpConnection? = null,
    bandwidths: List<SdpBandwidth> = emptyList(),
    timings: List<SdpTiming> = emptyList(),
    var timeZones: SdpTimeZones? = null,
    var key: EncryptionKey? = null,
    attributes: List<SdpAttribute> = emptyList(),
    mediaDescriptions: List<MediaDescription> = emptyList()
) : SdpElement {
    var uris = uris.toMutableList()
    var emails = emails.toMutableList()
    var phones = phones.toMutableList()
    var bandwidths = bandwidths.toMutableList()
    var timings = timings.toMutableList()
    var attributes = attributes.toMutableList()
    var mediaDescriptions = mediaDescriptions.toMutableList()

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
        @JvmStatic
        fun parse(text: String): SessionDescription {
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
            val mediaDescriptions = arrayListOf<MediaDescription>()
            var lastTiming: SdpTiming? = null
            var lastMediaDescription: MediaDescription? = null

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
                            list.add(BaseSdpAttribute.parse(line))
                        }
                        "m=" -> {
                            MediaDescription.parse(line).also {
                                lastMediaDescription = it
                                mediaDescriptions.add(it)
                            }
                        }
                        else -> {
                            println("Unknown Media: $line")
                        }
                    }
                }

            return SessionDescription(
                version = checkNotNull(version),
                origin = checkNotNull(origin),
                sessionName = checkNotNull(sessionName),
                information = information,
                uris = uris,
                emails = emails,
                phones = phones,
                connection = connection,
                bandwidths = bandwidths,
                timings = timings,
                timeZones = timeZones,
                key = key,
                attributes = attributes,
                mediaDescriptions = mediaDescriptions
            )
        }
    }
}