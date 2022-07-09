package com.yuyakaido.gaia.core.infra

import retrofit2.http.GET

interface AccountApi {

    @GET("api/v1/me")
    suspend fun getMe(): AccountResponse

}