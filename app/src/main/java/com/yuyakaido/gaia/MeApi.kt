package com.yuyakaido.gaia

import kotlinx.serialization.json.JsonElement
import retrofit2.http.GET

interface MeApi {

    @GET("me")
    suspend fun getMe(): JsonElement

}