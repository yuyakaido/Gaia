package com.yuyakaido.gaia.article.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.article.domain.ArticleRepository
import com.yuyakaido.gaia.core.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    data class State(
        val articles: List<Article> = emptyList(),
        val isRefreshing: Boolean = false
    )

    private val ids = MutableStateFlow(emptyList<Article.ID>())
    private val articles = ids.flatMapLatest { repository.observeArticles(it) }
    private val isRefreshing = MutableStateFlow(false)
    private val isPaginating = MutableStateFlow(false)

    val state = combine(
        articles, isRefreshing
    ) { articles, isRefreshing ->
        State(
            articles = articles,
            isRefreshing = isRefreshing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State()
    )

    init {
        onRefresh()
    }

    private suspend fun refresh() {
        if (!isRefreshing.value) {
            isRefreshing.value = true
            paginate(refresh = true)
            isRefreshing.value = false
        }
    }

    private suspend fun paginate(refresh: Boolean = false) {
        if (!isPaginating.value) {
            isPaginating.value = true
            if (refresh) {
                ids.value = emptyList()
            }
            val articles = repository.paginate(after = ids.value.lastOrNull()?.forPagination())
            ids.value = ids.value.plus(articles.map { it.id })
            isPaginating.value = false
        }
    }

    fun onRefresh() {
        viewModelScope.launch { refresh() }
    }

    fun onPaginate() {
        viewModelScope.launch { paginate() }
    }

}