package com.yuyakaido.gaia.article.domain

import com.yuyakaido.gaia.article.infra.ArticleRemoteDataSource
import com.yuyakaido.gaia.core.domain.Article
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val remote: ArticleRemoteDataSource
) {

    private val articles = MutableStateFlow(emptyMap<Article.ID, Article>())

    private fun emitArticle(newArticle: Article) {
        emitArticles(listOf(newArticle))
    }

    private fun emitArticles(newArticles: List<Article>) {
        articles.update {
            it.plus(newArticles.map { article -> article.id to article })
        }
    }

    fun observeArticle(id: Article.ID): Flow<Article> {
        return articles.mapNotNull { it[id] }
    }

    fun observeArticles(ids: List<Article.ID>): Flow<List<Article>> {
        return articles.map { ids.mapNotNull { id -> it[id] } }
    }

    suspend fun paginate(sort: ArticleSort, after: Article.ID?): Result<List<Article>> {
        return remote.getPopularArticles(sort, after)
            .onSuccess { emitArticles(it) }
    }

    suspend fun toggleVote(article: Article): Result<Article> {
        return when (article.likes) {
            true -> remote.unvote(article)
            false -> remote.vote(article)
            null -> remote.vote(article)
        }.onSuccess { emitArticle(it) }
    }

}