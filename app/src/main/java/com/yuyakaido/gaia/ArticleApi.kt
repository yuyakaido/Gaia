package com.yuyakaido.gaia

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("r/popular")
    suspend fun getPopularArticles(
        @Query("limit") limit: Int = 100
    ): ListingDataResponse

}