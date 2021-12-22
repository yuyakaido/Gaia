package com.yuyakaido.gaia

import retrofit2.http.GET

interface ArticleApi {

    @GET("r/popular")
    suspend fun getPopularArticles(): ListingDataResponse

}