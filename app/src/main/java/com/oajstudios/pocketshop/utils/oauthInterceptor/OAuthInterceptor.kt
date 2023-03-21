package com.oajstudios.pocketshop.utils.oauthInterceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class OAuthInterceptor constructor(consumerKey: String, consumerSecret: String, tokenSecret: String, token: String) : Interceptor {
    private val consumerKey: String
    private val consumerSecret: String
    private val tokenSecret: String
    private val token: String

    init {
        this.consumerKey = consumerKey
        this.consumerSecret = consumerSecret
        this.tokenSecret = tokenSecret
        this.token = token
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val nonce = TimestampServiceImpl().nonce
        val timestamp = TimestampServiceImpl().timestampInSeconds
        val dynamicStructureUrl = original.url.scheme + "://" + original.url.host + original.url.encodedPath
        val firstBaseString = original.method + "&" + urlEncoded(dynamicStructureUrl)
        var generatedBaseString: String
        generatedBaseString = if (original.url.encodedQuery != null) {
            original.url.encodedQuery + "&oauth_consumer_key=$consumerKey&oauth_nonce=$nonce&oauth_token=$token&oauth_signature_method=HMAC-SHA1&oauth_timestamp=$timestamp&oauth_version=1.0"
        } else {
            "oauth_consumer_key=$consumerKey&oauth_nonce=$nonce&oauth_token=$token&oauth_signature_method=HMAC-SHA1&oauth_timestamp=$timestamp&oauth_version=1.0"
        }
        val result = ParameterList()
        result.addQuerystring(generatedBaseString)
        generatedBaseString = result.sort().asOauthBaseString()
        var secoundBaseString = "&$generatedBaseString"
        if (firstBaseString.contains("%3F")) {
            secoundBaseString = "%26" + urlEncoded(generatedBaseString)
        }
        val baseString = firstBaseString + secoundBaseString
        val signature = HMACSha1SignatureService().getSignature(baseString, consumerSecret, tokenSecret)

        Log.d("Signature", signature)
        val url = originalHttpUrl.newBuilder()
                .addQueryParameter(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE)
                .addQueryParameter(OAUTH_CONSUMER_KEY, consumerKey)
                .addQueryParameter(OAUTH_TOKEN, token)
                .addQueryParameter(OAUTH_VERSION, OAUTH_VERSION_VALUE)
                .addQueryParameter(OAUTH_TIMESTAMP, timestamp)
                .addQueryParameter(OAUTH_NONCE, nonce)
                .addQueryParameter(OAUTH_SIGNATURE, signature)
                .build()
        // Request customization: add request headers
        val requestBuilder = original.newBuilder()
                .url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    class Builder {
        private var consumerKey: String = ""
        private var consumerSecret: String = ""
        private var tokenSecret: String = ""
        private var token: String = ""
        private val type: Int = 0

        fun consumerKey(consumerKey: String): Builder {
            this.consumerKey = consumerKey
            return this
        }

        fun consumerSecret(consumerSecret: String): Builder {
            this.consumerSecret = consumerSecret
            return this
        }

        fun tokenSecret(tokenSecret: String): Builder {
            this.tokenSecret = tokenSecret
            return this
        }

        fun token(token: String): Builder {
            this.token = token
            return this
        }

        fun build(): OAuthInterceptor {
            return OAuthInterceptor(consumerKey, consumerSecret, tokenSecret, token)
        }
    }

    private fun urlEncoded(url: String): String {
        var encodedurl = ""
        try {
            encodedurl = URLEncoder.encode(url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return encodedurl
    }

    companion object {
        private const val OAUTH_CONSUMER_KEY = "oauth_consumer_key"
        private const val OAUTH_NONCE = "oauth_nonce"
        private const val OAUTH_SIGNATURE = "oauth_signature"
        private const val OAUTH_SIGNATURE_METHOD = "oauth_signature_method"
        private const val OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1"
        private const val OAUTH_TIMESTAMP = "oauth_timestamp"
        private const val OAUTH_VERSION = "oauth_version"
        private const val OAUTH_VERSION_VALUE = "1.0"
        private const val OAUTH_TOKEN = "oauth_token"
    }
}