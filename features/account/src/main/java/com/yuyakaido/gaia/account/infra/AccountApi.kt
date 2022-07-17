package com.yuyakaido.gaia.account.infra

import retrofit2.http.GET

interface AccountApi {

    @GET("api/v1/me")
    suspend fun getMe(): AccountResponse

}