package com.yuyakaido.gaia.article

import com.yuyakaido.gaia.domain.Article
import com.yuyakaido.gaia.session.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalSerializationApi
@Singleton
class ArticleRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    private val articles = MutableStateFlow(emptyList<Article>())

    fun observeArticles(): StateFlow<List<Article>> {
        return articles
    }

    fun observeArticle(id: String): Flow<Article> {
        return articles.mapNotNull { it.firstOrNull { article -> article.id == id } }
    }

    suspend fun paginate(
        after: String?,
        refresh: Boolean = false
    ) {
        if (refresh) {
            articles.emit(emptyList())
        }
        val result = apiClient.getArticleApi()
            .getPopularArticles(after = after)
            .toArticles()
        articles.emit(articles.value.plus(result.items))
    }

}