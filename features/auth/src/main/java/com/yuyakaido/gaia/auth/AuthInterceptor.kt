package com.yuyakaido.gaia.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionRepository: SessionRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            val session = sessionRepository.getActiveSession()
            session?.let {
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", it.bearerToken)
                        .build()
                )
            } ?: chain.proceed(chain.request())
        }
    }

}