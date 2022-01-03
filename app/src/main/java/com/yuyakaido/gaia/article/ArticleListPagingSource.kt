package com.yuyakaido.gaia.article

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yuyakaido.gaia.domain.Article
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class ArticleListPagingSource(
    private val repository: ArticleRepository
) : PagingSource<String, Article>() {

    override fun getRefreshKey(
        state: PagingState<String, Article>
    ): String? {
        return null
    }

    override suspend fun load(
        params: LoadParams<String>
    ): LoadResult<String, Article> {
        return try {
            val result = repository.getPopularArticles(after = params.key)
            LoadResult.Page(
                data = result.items,
                prevKey = result.before,
                nextKey = result.after
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}