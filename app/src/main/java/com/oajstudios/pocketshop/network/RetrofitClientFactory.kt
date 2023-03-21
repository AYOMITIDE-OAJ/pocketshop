package com.oajstudios.pocketshop.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.oajstudios.pocketshop.WooBoxApp.Companion.getAppInstance
import com.oajstudios.pocketshop.utils.Constants.SharedPref.CONSUMERKEY
import com.oajstudios.pocketshop.utils.Constants.SharedPref.CONSUMERSECRET
import com.oajstudios.pocketshop.utils.extensions.getSharedPrefInstance
import com.oajstudios.pocketshop.utils.oauthInterceptor.OAuthInterceptor
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class RetrofitClientFactory(var url: String) {

    fun getRestApis(): RestApis {
        return getRetroFitClient().create(RestApis::class.java)
    }

    private fun getRetroFitClient(): Retrofit {
        val cacheSize = 50 * 1024
        val cache = Cache(File(getAppInstance().cacheDir, "responses"), cacheSize.toLong())

        val oauth1WooCommerce = OAuthInterceptor.Builder()
            .consumerKey(getSharedPrefInstance().getStringValue(CONSUMERKEY))
            .consumerSecret(getSharedPrefInstance().getStringValue(CONSUMERSECRET))
            .build()


        val builder = OkHttpClient().newBuilder().connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
        val okHttpClient = builder
            .addInterceptor(ResponseInterceptor())
            .addInterceptor(UnauthorizedInterceptor())
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request =
                        chain.request().newBuilder().addHeader(
                            "consumerKey",
                            getSharedPrefInstance().getStringValue(CONSUMERKEY)
                        ).addHeader(
                            "consumerSecret",
                            getSharedPrefInstance().getStringValue(CONSUMERSECRET)
                        ).build()
                    return chain.proceed(request)
                }
            })
            .addInterceptor(oauth1WooCommerce)
            .cache(cache)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .disableHtmlEscaping()
            .create()

        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }
}

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder()
            .body(
                ResponseBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    response.body!!.bytes()
                )
            )
            .build()
    }
}

internal class UnauthorizedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        Log.e("ddad", response.body.toString())
        return response
    }
}

