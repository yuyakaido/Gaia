package com.yuyakaido.gaia.auth

import com.yuyakaido.gaia.session.SessionRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

@ExperimentalSerializationApi
class TokenAuthenticator(
    private val authApi: AuthApi,
    private val sessionRepository: SessionRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val oldSession = sessionRepository.getActiveSession()
            oldSession?.token?.refreshToken?.let {
                val newToken = authApi.refreshAccessToken(refreshToken = it).toToken()
                val newSession = oldSession.copy(token = newToken)
                sessionRepository.putSession(newSession)
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