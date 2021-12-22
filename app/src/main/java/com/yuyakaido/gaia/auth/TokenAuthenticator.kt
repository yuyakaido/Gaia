package com.yuyakaido.gaia.auth

import android.app.Application
import com.yuyakaido.gaia.Networking
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.*

@ExperimentalSerializationApi
class TokenAuthenticator(
    private val application: Application
) : Authenticator {

    private val api = Networking.createAuthApi()

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val oldSession = Session.get(application)
            oldSession?.refreshToken?.let {
                val result = api.refreshAccessToken(refreshToken = it)
                val newSession = result.toSession()
                Session.put(application, newSession)
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