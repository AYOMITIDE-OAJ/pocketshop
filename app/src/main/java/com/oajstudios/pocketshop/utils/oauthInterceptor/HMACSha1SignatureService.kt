package com.oajstudios.pocketshop.utils.oauthInterceptor

import android.util.Base64
import android.util.Log

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class HMACSha1SignatureService(override val signatureMethod: String = METHOD) : SignatureService {

    override fun getSignature(baseString: String, apiSecret: String, tokenSecret: String): String {
        try {
            Preconditions.checkEmptyString(baseString, "Base string cant be null or empty string")
            Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string")
            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&'.toString() + OAuthEncoder.encode(tokenSecret))
        } catch (e: Exception) {
            throw OAuthSignatureException(baseString, e)
        }
    }

    @Throws(Exception::class)
    private fun doSign(toSign: String, keyString: String): String {
        Log.d("is it signing", "----------------------$toSign")
        Log.d("is 22222222", keyString + "")
        val key = SecretKeySpec(keyString.toByteArray(charset(UTF8)), HMAC_SHA1)
        val mac = Mac.getInstance(HMAC_SHA1)
        mac.init(key)
        val bytes = mac.doFinal(toSign.toByteArray(charset(UTF8)))
        return bytesToBase64String(bytes).replace(CARRIAGE_RETURN, "")
    }

    private fun bytesToBase64String(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    companion object {
        private val CARRIAGE_RETURN = "\r\n"
        private val UTF8 = "UTF-8"
        private val HMAC_SHA1 = "HmacSHA1"
        private val METHOD = "HMAC-SHA1"
    }
}