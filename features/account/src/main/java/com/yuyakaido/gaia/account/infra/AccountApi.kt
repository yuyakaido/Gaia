package com.yuyakaido.gaia.account.infra

import com.yuyakaido.gaia.core.infra.ListResponse
import com.yuyakaido.gaia.core.infra.ObjectResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountApi {

    @GET("api/v1/me")
    suspend fun getMe(): MeResponse

    @GET("user/{name}/about")
    suspend fun getUser(
        @Path("name") name: String
    ): ObjectResponse

    @GET("user/{name}/submitted")
    suspend fun getPosts(
        @Path("name") name: String,
        @Query("sr_detail") srDetail: Boolean = true
    ): ListResponse

}