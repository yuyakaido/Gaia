package com.yuyakaido.gaia.article

import com.yuyakaido.gaia.core.ListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("r/popular")
    suspend fun getPopularArticles(
        @Query("after") after: String?
    ): ListResponse

}