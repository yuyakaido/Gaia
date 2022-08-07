package com.yuyakaido.gaia.article.domain

import com.yuyakaido.gaia.article.infra.ArticleLocalDataSource
import com.yuyakaido.gaia.article.infra.ArticleRemoteDataSource
import com.yuyakaido.gaia.core.domain.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val remote: ArticleRemoteDataSource,
    private val local: ArticleLocalDataSource,
    private val communityRepository: CommunityRepository
) {

    fun observeArticle(id: Article.ID): Flow<Article> {
        return local.observeArticle(id)
            .map { article ->
                val community = communityRepository.getCommunity(article.community.name)
                article.copy(community = community)
            }
    }

    fun observeArticles(ids: List<Article.ID>): Flow<List<Article>> {
        return local.observeArticles(ids)
            .map { articles ->
                articles.map { article ->
                    val community = communityRepository.getCommunity(article.community.name)
                    article.copy(community = community)
                }
            }
    }

    suspend fun paginate(sort: ArticleSort, after: Article.ID?): Result<List<Article>> {
        return remote.getPopularArticles(sort, after)
            .onSuccess { local.emitArticles(it) }
            .onSuccess { articles ->
                val names = articles.map { it.community.name }.distinct()
                communityRepository.refreshCommunities(names)
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