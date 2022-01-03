package com.yuyakaido.gaia.article

import com.yuyakaido.gaia.core.ListingResult
import com.yuyakaido.gaia.domain.Article
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalSerializationApi
@Singleton
class ArticleRepository @Inject constructor(
    private val api: ArticleApi
) {

    private val cache = mutableSetOf<Article>()

    suspend fun getPopularArticles(
        after: String?
    ): ListingResult<Article> {
        val result = api
            .getPopularArticles(after = after)
            .toArticles()
        cache.addAll(result.items)
        return result
    }

    fun getArticle(id: String): Article {
        return cache.first { it.id == id }
    }

}