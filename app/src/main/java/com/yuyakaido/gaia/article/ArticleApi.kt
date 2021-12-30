package com.yuyakaido.gaia.article

import com.yuyakaido.gaia.core.ListResponse
import retrofit2.http.GET

interface ArticleApi {

    @GET("r/popular")
    suspend fun getPopularArticles(): ListResponse

}