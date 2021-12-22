package com.yuyakaido.gaia.auth

import com.yuyakaido.gaia.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {

    private val credential = Credentials.basic(BuildConfig.REDDIT_CLIENT_ID, "")

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", credential)
                .build()
        )
    }

}