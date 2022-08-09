package com.yuyakaido.gaia.auth.domain

import com.yuyakaido.gaia.auth.infra.AuthRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val remote: AuthRemoteDataSource
) {

    suspend fun getAccessToken(code: String): Result<Token> {
        return remote.getAccessToken(code)
    }

    suspend fun refreshAccessToken(refreshToken: String): Result<Token> {
        return remote.refreshAccessToken(refreshToken)
    }

}