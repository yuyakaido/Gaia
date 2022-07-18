package com.yuyakaido.gaia.article.infra

import com.yuyakaido.gaia.article.domain.ArticleSort
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.infra.ApiErrorHandler
import com.yuyakaido.gaia.core.infra.ApiExecutor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRemoteDataSource @Inject constructor(
    override val apiErrorHandler: ApiErrorHandler,
    private val api: ArticleApi
) : ApiExecutor {

    suspend fun getPopularArticles(
        sort: ArticleSort,
        after: Article.ID?
    ): Result<List<Article>> {
        return execute {
            val response = api.getArticlesBySort(
                sort = sort.path,
                after = after?.forPagination()
            )
            response.toArticles().items
        }
    }

}