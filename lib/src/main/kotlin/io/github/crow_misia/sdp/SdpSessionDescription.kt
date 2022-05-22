package io.github.crow_misia.sdp

import io.github.crow_misia.sdp.attribute.SdpAttribute

/**
 * Session Description.
 *
 * session-description = version-field
 *                       origin-field
 *                       session-name-field
 *                       [information-field]
 *                       [uri-field]
 *                        *email-field
 *                        *phone-field
 *                       [connection-field]
 *                       *bandwidth-field
 *                       1*time-description
 *                       [key-field]
 *                       *attribute-field
 *                       *media-description
 *
 * version-field =       %s"v" "=" 1*DIGIT CRLF
 *                           ;this memo describes version 0
 *
 * origin-field =       %s"o" "=" username SP sess-id SP sess-version SP nettype SP addrtype SP unicast-address CRLF
 *
 * session-name-field =  %s"s" "=" text CRLF
 *
 * information-field =   %s"i" "=" text CRLF
 *
 * uri-field =           %s"u" "=" uri CRLF
 *
 * email-field =         %s"e" "=" email-address CRLF
 *
 * phone-field =         %s"p" "=" phone-number CRLF
 *
 * connection-field =    %s"c" "=" nettype SP addrtype SP connection-address CRLF
 *                           ;a connection field must be present
 *                           ;in every media description or at the
 *                           ;session level
 *
 * bandwidth-field =     %s"b" "=" bwtype ":" bandwidth CRLF
 *
 * time-description = time-field [repeat-description]
 *
 * repeat-description =  1*repeat-field [zone-field]
 *
 * time-field =          %s"t" "=" start-time SP stop-time CRLF
 *
 * repeat-field =        %s"r" "=" repeat-interval SP typed-time 1*(SP typed-time) CRLF
 *
 * zone-field =          %s"z" "=" time SP ["-"] typed-time  *(SP time SP ["-"] typed-time) CRLF
 *
 * key-field =           %s"k" "=" key-type CRLF
 *
 * attribute-field =     %s"a" "=" attribute CRLF
 *
 * media-description = media-field [information-field] *connection-field *bandwidth-field [key-field] *attribute-field
 *
 * media-field =         %s"m" "=" media SP port ["/" integer] SP proto 1*(SP fmt) CRLF
 *
 * ; sub-rules of 'o='
 * username =            non-ws-string
 *                       ;pretty wide definition, but doesn't include space
 *
 * sess-id =             1*DIGIT
 *                       ;should be unique for this username/host
 *
 * sess-version =        1*DIGIT
 *
 * nettype = token ;typically "IN"
 *
 * addrtype = token ;typically "IP4" or "IP6"
 *
 * ; sub-rules of 'u='
 * uri = URI-reference ; see RFC 3986
 *
 * ; sub-rules of 'e=', see RFC 5322 for definitions
 * email-address        = address-and-comment / dispname-and-address / addr-spec
 * address-and-comment  = addr-spec 1*SP "(" 1*email-safe ")"
 * dispname-and-address = 1*email-safe 1*SP "<" addr-spec ">"
 *
 * ; sub-rules of 'p='
 * phone-number =        phone *SP "(" 1*email-safe ")" / 1*email-safe "<" phone ">" / phone
 * phone =               ["+"] DIGIT 1*(SP / "-" / DIGIT)
 *
 * ; sub-rules of 'c='
 * connection-address =  multicast-address / unicast-address
 *
 * ; sub-rules of 'b=' bwtype = token
 * bandwidth =           1*DIGIT
 *
 * ; sub-rules of 't='
 * start-time =          time / "0"
 *
 * stop-time = time / "0"
 *
 * time =                POS-DIGIT 9*DIGIT
 *                       ; Decimal representation of time in
 *                       ; seconds since January 1, 1900 UTC.
 *                       ; The representation is an unbounded
 *                       ; length field containing at least
 *                       ; 10 digits. Unlike some representations
 *                       ; used elsewhere, time in SDP does not
 *                       ; wrap in the year 2036.
 *
 * ; sub-rules of 'r=' and 'z='
 * repeat-interval =     POS-DIGIT *DIGIT [fixed-len-time-unit]
 *
 * typed-time =          1*DIGIT [fixed-len-time-unit]
 *
 * fixed-len-time-unit = %s"d" / %s"h" / %s"m" / %s"s"
 * ; NOTE: These units are case-sensitive.
 *
 * ; sub-rules of 'k='
 * key-type =            %s"prompt" / %s"clear:" text / %s"base64:" base64 / %s"uri:" uri
 *                       ; NOTE: These names are case-sensitive.
 *
 * base64      =         *base64-unit [base64-pad]
 * base64-unit =         4base64-char
 * base64-pad  =         2base64-char "==" / 3base64-char "="
 * base64-char =         ALPHA / DIGIT / "+" / "/"
 *
 * ; sub-rules of 'a='
 * attribute =           (attribute-name ":" attribute-value) / attribute-name
 * attribute-name =      token
 * attribute-value =     byte-string
 * att-field = attribute-name ; for backward compatibility
 *
 * ; sub-rules of 'm='
 * media =               token ;typically "audio", "video", "text", "image" or "application"
 * fmt = token ;typically an RTP payload type for audio ;and video media
 * proto  =              token *("/" token) ;typically "RTP/AVP", "RTP/SAVP", "udp", or "RTP/SAVPF"
 * port =                1*DIGIT
 *
 * ; generic sub-rules: addressing
 * unicast-address =     IP4-address / IP6-address / FQDN / extn-addr
 * multicast-address =   IP4-multicast / IP6-multicast / FQDN / extn-addr
 *
 * IP4-multicast =       m1 3( "." decimal-uchar ) "/" ttl [ "/" numaddr ]
 * ; IP4 multicast addresses may be in the
 * ; range 224.0.0.0 to 239.255.255.255
 *
 * m1 =                  ("22" ("4"/"5"/"6"/"7"/"8"/"9")) / ("23" DIGIT )
 *
 * IP6-multicast = IP6-address [ "/" numaddr ] ; IP6 address starting with FF
 *
 * numaddr =             integer
 * ttl =                 (POS-DIGIT *2DIGIT) / "0"
 * FQDN =                4*(alpha-numeric / "-" / ".")
 * ; fully qualified domain name as specified
 * ; in RFC 1035 (and updates)
 *
 * IP4-address = b1 3("." decimal-uchar)
 * IP4-Address = B1 3（ "。" DECIMAL-UCHAR）
 *
 * b1 = decimal-uchar ; less than "224"
 *
 * IP6-address =         6( h16 ":" ) ls32
 *                       /                       "::" 5( h16 ":" ) ls32
 *                       / [               h16 ] "::" 4( h16 ":" ) ls32
 *                       / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
 *                       / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
 *                       / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
 *                       / [ *4( h16 ":" ) h16 ] "::"              ls32
 *                       / [ *5( h16 ":" ) h16 ] "::"              h16
 *                       / [ *6( h16 ":" ) h16 ] "::"
 *
 * h16 =                 1*4HEXDIG
 *
 * ls32 =                ( h16 ":" h16 ) / IP4-address
 *
 * ; Generic for other address families extn-addr = non-ws-string
 *
 * ; generic sub-rules: datatypes
 * text =                byte-string
 *                       ;default is to interpret this as UTF8 text.
 *                       ;ISO 8859-1 requires "a=charset:ISO-8859-1"
 *                       ;session-level attribute to be used
 *
 * byte-string =         1*(%x01-09/%x0B-0C/%x0E-FF)
 *                       ;any byte except NUL, CR, or LF
 *
 * non-ws-string =       1*(VCHAR/%x80-FF)
 *                       ;string of visible characters
 *
 * token-char =          ALPHA / DIGIT
 *                       / "!" / "#" / "$" / "%" / "&"
 *                       / "'" ; (single quote)
 *                       / "*" / "+" / "-" / "." / "^" / "_"
 *                       / "`" ; (Grave accent)
 *                       / "{" / "|" / "}" / "~"
 *
 * token =               1*(token-char)
 *
 * email-safe =          %x01-09/%x0B-0C/%x0E-27/%x2A-3B/%x3D/%x3F-FF
 *                       ;any byte except NUL, CR, LF, or the quoting characters ()<>
 *
 * integer = POS-DIGIT *DIGIT
 *
 * zero-based-integer = "0" / integer
 *
 * non-zero-int-or-real = integer / non-zero-real
 *
 * non-zero-real = zero-based-integer "." *DIGIT POS-DIGIT
 *
 * ; generic sub-rules: primitives
 * alpha-numeric =       ALPHA / DIGIT
 *
 * POS-DIGIT =           %x31-39 ; 1 - 9
 *
 * decimal-uchar =       DIGIT
 *                       / POS-DIGIT DIGIT
 *                       / ("1" 2(DIGIT))
 *                       / ("2" ("0"/"1"/"2"/"3"/"4") DIGIT)
 *                       / ("2" "5" ("0"/"1"/"2"/"3"/"4"/"5"))
 *
 * ; external references:
 * ALPHA =               <ALPHA definition from RFC 5234>
 * DIGIT =               <DIGIT definition from RFC 5234>
 * CRLF =                <CRLF definition from RFC 5234>
 * HEXDIG =              <HEXDIG definition from RFC 5234>
 * SP =                  <SP definition from RFC 5234>
 * VCHAR =               <VCHAR definition from RFC 5234>
 * URI-reference =       <URI-reference definition from RFC 3986>
 * addr-spec =           <addr-spec definition from RFC 5322>
 */
data class SdpSessionDescription internal constructor(
    var version: SdpVersion,
    var origin: SdpOrigin,
    var sessionName: SdpSessionName,
    var information: SdpSessionInformation?,
    var uris: MutableList<SdpUri>,
    var emails: MutableList<SdpEmail>,
    var phones: MutableList<SdpPhone>,
    var connection: SdpConnection?,
    var bandwidths: MutableList<SdpBandwidth>,
    var timings: MutableList<SdpTimeActive>,
    var timeZones: SdpTimeZones?,
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
            timings: List<SdpTimeActive> = emptyList(),
            timeZones: SdpTimeZones? = null,
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
            val timings = arrayListOf<SdpTimeActive>()
            var timeZones: SdpTimeZones? = null
            val attributes = arrayListOf<SdpAttribute>()
            val mediaDescriptions = arrayListOf<SdpMediaDescription>()
            var lastTiming: SdpTimeActive? = null
            var lastMediaDescription: SdpMediaDescription? = null

            text.splitToSequence("\r\n", "\n", "\r")
                .map { it.trim() }
                .forEach { line ->
                    if (line.isBlank()) {
                        return@forEach
                    }
                    when (line.substring(0, 2)) {
                        SdpVersion.lineType -> version = SdpVersion.parse(line)
                        SdpOrigin.lineType -> origin = SdpOrigin.parse(line)
                        SdpSessionName.lineType -> sessionName = SdpSessionName.parse(line)
                        SdpSessionInformation.lineType -> {
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
                        SdpTimeActive.fieldPart -> {
                            SdpTimeActive.parse(line).also { v ->
                                lastTiming = v
                                timings.add(v)
                            }
                        }
                        SdpTimeZones.fieldPart -> timeZones = SdpTimeZones.parse(line)
                        SdpRepeatTimes.fieldPart -> lastTiming?.repeatTime = SdpRepeatTimes.parse(line)
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
