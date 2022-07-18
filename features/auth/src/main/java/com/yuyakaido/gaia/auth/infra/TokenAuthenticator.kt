package com.yuyakaido.gaia.auth.infra

import com.yuyakaido.gaia.auth.domain.AuthRepository
import com.yuyakaido.gaia.auth.domain.SessionRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val oldSession = sessionRepository.getActiveSession()
            oldSession?.token?.refreshToken?.let {
                val newToken = authRepository.refreshAccessToken(refreshToken = it)
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