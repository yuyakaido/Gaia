package com.yuyakaido.gaia.article.infra

import com.yuyakaido.gaia.core.infra.ListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleApi {

    @GET("{sort}")
    suspend fun getArticlesBySort(
        @Path("sort") sort: String,
        @Query("after") after: String?
    ): ListResponse

}