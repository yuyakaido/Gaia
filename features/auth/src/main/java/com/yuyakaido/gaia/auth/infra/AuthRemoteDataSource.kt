package com.yuyakaido.gaia.auth.infra

import com.yuyakaido.gaia.auth.domain.Token
import com.yuyakaido.gaia.core.infra.ApiErrorHandler
import com.yuyakaido.gaia.core.infra.ApiExecutor
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    override val apiErrorHandler: ApiErrorHandler,
    private val api: AuthApi
) : ApiExecutor {

    suspend fun getAccessToken(code: String): Result<Token> {
        return execute {
            api.getAccessToken(code = code).toToken()
        }
    }

    suspend fun refreshAccessToken(refreshToken: String): Result<Token> {
        return execute {
            api.refreshAccessToken(refreshToken = refreshToken).toToken()
        }
    }

}