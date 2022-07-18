package com.yuyakaido.gaia.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null
) {
    fun toToken(): Token {
        return Token(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}