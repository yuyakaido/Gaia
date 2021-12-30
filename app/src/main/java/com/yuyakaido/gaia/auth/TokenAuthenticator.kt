package com.yuyakaido.gaia.auth

import android.app.Application
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.*

@ExperimentalSerializationApi
class TokenAuthenticator(
    private val application: Application,
    private val authApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val oldSession = Session.get(application = application)
            oldSession?.token?.refreshToken?.let {
                val token = authApi.refreshAccessToken(refreshToken = it).toToken()
                val newSession = Session(
                    id = oldSession.id,
                    token = token
                )
                Session.put(
                    application = application,
                    session = newSession
                )
                return@runBlocking response
                    .request
                    .newBuilder()
                    .header("Authorization", newSession.bearerToken)
                    .build()
            }
            return@runBlocking null
        }
    }

}