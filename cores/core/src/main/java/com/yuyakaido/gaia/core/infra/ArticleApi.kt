package com.yuyakaido.gaia.core.infra

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("r/popular")
    suspend fun getPopularArticles(
        @Query("after") after: String?
    ): ListResponse

}