package com.oajstudios.pocketshop.utils.oauthInterceptor

import java.util.regex.Pattern

object Preconditions {
    private val DEFAULT_MESSAGE = "Received an invalid parameter"

    // scheme = alpha *( alpha | digit | "+" | "-" | "." )
    private val URL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*://\\S+")

    fun checkNotNull(`object`: Any?, errorMsg: String) {
        check(`object` != null, errorMsg)
    }

    fun checkEmptyString(string: String?, errorMsg: String) {
        check(string != null && string.trim { it <= ' ' } != "", errorMsg)
    }

    fun checkValidUrl(url: String, errorMsg: String) {
        checkEmptyString(url, errorMsg)
        check(isUrl(url), errorMsg)
    }

    fun checkValidOAuthCallback(url: String, errorMsg: String) {
        checkEmptyString(url, errorMsg)
        if (url.toLowerCase().compareTo(OAuthConstants.OUT_OF_BAND, ignoreCase = true) != 0) {
            check(isUrl(url), errorMsg)
        }
    }

    private fun isUrl(url: String): Boolean {
        return URL_PATTERN.matcher(url).matches()
    }

    private fun check(requirements: Boolean, error: String?) {
        val message = if (error == null || error.trim { it <= ' ' }.isEmpty()) DEFAULT_MESSAGE else error
        if (!requirements) {
            throw IllegalArgumentException(message)
        }
    }
}