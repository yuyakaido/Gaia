package com.yuyakaido.gaia.user

import kotlinx.serialization.json.JsonElement
import retrofit2.http.GET

interface MeApi {

    @GET("api/v1/me")
    suspend fun getMe(): JsonElement

}