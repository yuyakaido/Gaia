package com.yuyakaido.gaia.article.infra

import com.yuyakaido.gaia.core.infra.ListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("r/popular")
    suspend fun getPopularArticles(
        @Query("after") after: String?
    ): ListResponse

}