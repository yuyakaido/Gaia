package com.yuyakaido.gaia.message.infra

import com.yuyakaido.gaia.core.infra.ListResponse
import retrofit2.http.GET

interface MessageApi {

    @GET("message/messages")
    suspend fun getMessages(): ListResponse

}