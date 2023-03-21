package com.oajstudios.pocketshop.utils.oauthInterceptor

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import java.util.regex.Pattern

object OAuthEncoder {
    private val CHARSET = "UTF-8"
    private val ENCODING_RULES: Map<String, String>

    init {
        val rules = HashMap<String, String>()
        rules.put("*", "%2A")
        rules.put("+", "%20")
        rules.put("%7E", "~")
        ENCODING_RULES = Collections.unmodifiableMap<String, String>(rules)
    }

    fun encode(plain: String): String {
        // Preconditions.checkNotNull(plain, "Cannot encode null object");
        var encoded = ""
        try {
            encoded = URLEncoder.encode(plain, CHARSET)
        } catch (uee: UnsupportedEncodingException) {
            throw OAuthException(
                "Charset not found while encoding string: $CHARSET",
                uee
            )
        }
        for (rule in ENCODING_RULES.entries) {
            encoded = applyRule(encoded, rule.key, rule.value)
        }
        return encoded
    }

    private fun applyRule(encoded: String, toReplace: String, replacement: String): String {
        return encoded.replace((Pattern.quote(toReplace)).toRegex(), replacement)
    }

    fun decode(encoded: String): String {
        try {
            return URLDecoder.decode(encoded, CHARSET)
        } catch (uee: UnsupportedEncodingException) {
            throw OAuthException(
                "Charset not found while decoding string: $CHARSET",
                uee
            )
        }
    }
}