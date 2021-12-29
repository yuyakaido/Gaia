package com.yuyakaido.gaia

import android.app.Application
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
object ArticleRepository {

    private val cache = mutableSetOf<Article>()

    suspend fun getPopularArticles(application: Application): List<Article> {
        val articles = Networking
            .createArticleApi(application)
            .getPopularArticles()
            .toArticles()
        cache.addAll(articles)
        return articles
    }

    suspend fun getArticle(id: String): Article {
        return cache.first { it.id == id }
    }

}