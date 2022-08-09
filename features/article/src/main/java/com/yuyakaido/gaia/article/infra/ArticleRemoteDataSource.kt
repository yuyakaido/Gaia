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

    suspend fun getArticlesBySort(
        sort: ArticleSort,
        after: Article.ID?
    ): Result<List<Article>> {
        return execute {
            val response = api.getArticlesBySort(
                sort = sort.path,
                after = after?.full()
            )
            response.toArticles().items
        }
    }

    suspend fun vote(article: Article): Result<Article> {
        return execute {
            api.vote(id = article.id.full(), dir = 1)
            return@execute article.toVoted()
        }
    }

    suspend fun unvote(article: Article): Result<Article> {
        return execute {
            api.vote(id = article.id.full(), dir = 0)
            return@execute article.toUnvoted()
        }
    }

}