package com.yuyakaido.gaia.auth.domain

import com.yuyakaido.gaia.auth.infra.AuthApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi
) {

    suspend fun getAccessToken(code: String): Token {
        return api.getAccessToken(code = code).toToken()
    }

    suspend fun refreshAccessToken(refreshToken: String): Token {
        return api.refreshAccessToken(refreshToken = refreshToken).toToken()
    }

}