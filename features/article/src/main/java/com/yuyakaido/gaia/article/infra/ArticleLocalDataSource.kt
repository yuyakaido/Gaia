package com.yuyakaido.gaia.article.infra

import com.yuyakaido.gaia.core.domain.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ArticleLocalDataSource @Inject constructor() {

    private val cache = MutableStateFlow<Map<Article.ID, Article>>(emptyMap())

    fun observeArticle(id: Article.ID): Flow<Article> {
        return cache.mapNotNull { it[id] }
    }

    fun observeArticles(ids: List<Article.ID>): Flow<List<Article>> {
        return cache.map { articles -> ids.mapNotNull { id -> articles[id] } }
    }

    fun emitArticle(article: Article) {
        emitArticles(listOf(article))
    }

    fun emitArticles(articles: List<Article>) {
        cache.value = cache.value.plus(articles.map { it.id to it })
    }

}