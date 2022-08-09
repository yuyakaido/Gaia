package com.yuyakaido.gaia.account.infra

import com.yuyakaido.gaia.core.infra.ObjectResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountApi {

    @GET("api/v1/me")
    suspend fun getMe(): MeResponse

    @GET("user/{name}/about")
    suspend fun getUser(
        @Path("name") name: String
    ): ObjectResponse

}