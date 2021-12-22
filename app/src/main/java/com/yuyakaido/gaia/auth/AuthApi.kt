package com.yuyakaido.gaia.auth

import com.yuyakaido.gaia.auth.AccessTokenResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("access_token")
    suspend fun getAccessToken(
        @Query("grant_type") grantType: String = "authorization_code",
        @Query("redirect_uri") redirectUri: String = "com.yuyakaido.gaia://authorization",
        @Query("code") code: String
    ): AccessTokenResponse

    @POST("access_token")
    suspend fun refreshAccessToken(
        @Query("grant_type") grantType: String = "refresh_token",
        @Query("refresh_token") refreshToken: String
    ): AccessTokenResponse

}