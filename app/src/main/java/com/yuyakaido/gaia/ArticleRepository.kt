package com.yuyakaido.gaia

import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
@AppScope
class ArticleRepository @Inject constructor(
    private val api: ArticleApi
) {

    private val cache = mutableSetOf<Article>()

    suspend fun getPopularArticles(): List<Article> {
        val articles = api
            .getPopularArticles()
            .toArticles()
        cache.addAll(articles)
        return articles
    }

    suspend fun getArticle(id: String): Article {
        return cache.first { it.id == id }
    }

}