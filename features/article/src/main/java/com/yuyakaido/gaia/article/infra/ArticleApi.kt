package com.yuyakaido.gaia.article.infra

import com.yuyakaido.gaia.core.infra.ListResponse
import retrofit2.http.*

interface ArticleApi {

    @GET("{sort}")
    suspend fun getArticlesBySort(
        @Path("sort") sort: String,
        @Query("after") after: String?,
        @Query("sr_detail") srDetail: Boolean = true
    ): ListResponse

    @GET("r/{community}/comments/{article}")
    suspend fun getCommentsOfArticle(
        @Path("community") community: String,
        @Path("article") article: String,
        @Query("sr_detail") srDetail: Boolean = true
    ): List<ListResponse>

    @FormUrlEncoded
    @POST("api/vote")
    suspend fun vote(
        @Field("id") id: String,
        @Field("dir") dir: Int
    )

}