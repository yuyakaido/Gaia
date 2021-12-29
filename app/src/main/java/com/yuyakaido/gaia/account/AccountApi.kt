package com.yuyakaido.gaia.account

import retrofit2.http.GET

interface AccountApi {

    @GET("api/v1/me")
    suspend fun getMe(): AccountResponse

}