package com.yuyakaido.gaia.core.infra

import retrofit2.http.GET

interface MessageApi {

    @GET("message/messages")
    suspend fun getMessages(): ListResponse

}