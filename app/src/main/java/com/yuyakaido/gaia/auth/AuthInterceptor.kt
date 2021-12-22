package com.yuyakaido.gaia.auth

import android.app.Application
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val application: Application
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val session = Session.get(application)
        return session?.let {
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", it.bearerToken)
                    .build()
            )
        } ?: chain.proceed(chain.request())
    }

}