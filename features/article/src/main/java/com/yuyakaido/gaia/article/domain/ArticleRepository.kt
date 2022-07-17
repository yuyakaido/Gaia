package com.yuyakaido.gaia.article.domain

import com.yuyakaido.gaia.article.infra.ArticleApi
import com.yuyakaido.gaia.core.domain.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val api: ArticleApi
) {

    private val articles = MutableStateFlow(emptyMap<Article.ID, Article>())

    fun observeArticle(id: Article.ID): Flow<Article> {
        return articles.mapNotNull { it[id] }
    }

    fun observeArticles(ids: List<Article.ID>): Flow<List<Article>> {
        return articles.map { ids.mapNotNull { id -> it[id] } }
    }

    suspend fun paginate(after: String?): List<Article> {
        val result = api.getPopularArticles(after).toArticles()
        articles.emit(articles.value.plus(result.items.map { it.id to it }))
        return result.items
    }

}