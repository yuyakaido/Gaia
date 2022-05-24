package com.yuyakaido.gaia.auth

import com.yuyakaido.gaia.account.AccountRepository
import com.yuyakaido.gaia.session.SessionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val accountRepository: AccountRepository
) {

    suspend fun authorize(code: String) {
        val token = authRepository.getAccessToken(code)
        val session = sessionRepository.createSession(token)
        val account = accountRepository.refreshMe()
        sessionRepository.putSession(session.copy(name = account.name))
    }

}