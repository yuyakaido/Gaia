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
        val isRefreshing: Boolean = false,
        val isError: Boolean = false
    )

    private val ids = MutableStateFlow(emptyList<Article.ID>())
    private val articles = ids.flatMapLatest { repository.observeArticles(it) }
    private val isRefreshing = MutableStateFlow(false)
    private val isPaginating = MutableStateFlow(false)
    private val isError = MutableStateFlow(false)

    val state = combine(
        articles, isRefreshing, isError
    ) { articles, isRefreshing, isError ->
        State(
            articles = articles,
            isRefreshing = isRefreshing,
            isError = isError
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
            isError.value = false
            if (refresh) {
                ids.value = emptyList()
            }
            repository.paginate(after = ids.value.lastOrNull())
                .onSuccess { articles -> ids.value = ids.value.plus(articles.map { it.id }) }
                .onSuccess {
                    isPaginating.value = false
                    isError.value = false
                }
                .onFailure {
                    isPaginating.value = false
                    isError.value = true
                }
        }
    }

    fun onRefresh() {
        viewModelScope.launch { refresh() }
    }

    fun onPaginate() {
        viewModelScope.launch { paginate() }
    }

}