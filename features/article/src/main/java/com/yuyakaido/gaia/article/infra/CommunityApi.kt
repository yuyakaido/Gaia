package com.yuyakaido.gaia.article.infra

import retrofit2.http.GET
import retrofit2.http.Path

interface CommunityApi {

    @GET("r/{name}/about")
    suspend fun getCommunity(
        @Path("name") name: String
    ): CommunityResponse

}