package com.yuyakaido.gaia.article.domain

import com.yuyakaido.gaia.article.infra.ArticleLocalDataSource
import com.yuyakaido.gaia.article.infra.ArticleRemoteDataSource
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.domain.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val remote: ArticleRemoteDataSource,
    private val local: ArticleLocalDataSource
) {

    fun observeArticle(id: Article.ID): Flow<Article> {
        return local.observeArticle(id)
    }

    fun observeArticles(ids: List<Article.ID>): Flow<List<Article>> {
        return local.observeArticles(ids)
    }

    suspend fun paginate(sort: ArticleSort, after: Article.ID?): Result<List<Article>> {
        return remote.getArticlesBySort(sort, after)
            .onSuccess { local.emitArticles(it) }
    }

    suspend fun getComments(article: Article): Result<List<Comment.Article>> {
        return remote.getCommentsOfArticle(article)
            .onSuccess {
                local.emitArticle(
                    article.copy(
                        comments = it
                    )
                )
            }
    }

    suspend fun toggleVote(article: Article): Result<Article> {
        return when (article.likes) {
            true -> remote.unvote(article)
            false -> remote.vote(article)
            null -> remote.vote(article)
        }.onSuccess { local.emitArticle(it) }
    }

}