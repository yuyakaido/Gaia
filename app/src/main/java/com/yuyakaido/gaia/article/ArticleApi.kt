package com.yuyakaido.gaia.article

import com.yuyakaido.gaia.core.ListingDataResponse
import retrofit2.http.GET

interface ArticleApi {

    @GET("r/popular")
    suspend fun getPopularArticles(): ListingDataResponse

}