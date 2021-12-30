package com.yuyakaido.gaia.message

import com.yuyakaido.gaia.core.ListResponse
import retrofit2.http.GET

interface MessageApi {

    @GET("message/messages")
    suspend fun getMessages(): ListResponse

}