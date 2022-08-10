package com.yuyakaido.gaia.article.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.article.domain.ArticleRepository
import com.yuyakaido.gaia.article.domain.ArticleSort
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.presentation.ArticleContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    data class State(
        val contents: List<ArticleContent> = emptyList(),
        val isRefreshing: Boolean = false,
        val isError: Boolean = false
    )

    private val sort = MutableStateFlow(ArticleSort.Best)
    private val ids = MutableStateFlow(emptyList<Article.ID>())
    private val articles = ids.flatMapLatest { repository.observeArticles(it) }
    private val isRefreshing = MutableStateFlow(false)
    private val isPaginating = MutableStateFlow(false)
    private val isError = MutableStateFlow(false)
    private val processingArticle = MutableStateFlow<Article?>(null)

    val state = combine(
        articles, isRefreshing, isError, processingArticle
    ) { articles, isRefreshing, isError, processingArticle ->
        State(
            contents = articles.map {
                ArticleContent(
                    article = it,
                    isProcessing = processingArticle?.id == it.id
                )
            },
            isRefreshing = isRefreshing,
            isError = isError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State()
    )

    init {
        viewModelScope.launch {
            sort.collect {
                refresh(it)
            }
        }
    }

    private suspend fun refresh(sort: ArticleSort) {
        if (!isRefreshing.value) {
            isRefreshing.value = true
            paginate(sort = sort, refresh = true)
            isRefreshing.value = false
        }
    }

    private suspend fun paginate(sort: ArticleSort, refresh: Boolean = false) {
        if (!isPaginating.value) {
            isPaginating.value = true
            isError.value = false
            if (refresh) {
                ids.value = emptyList()
            }
            repository.paginate(sort = sort, after = ids.value.lastOrNull())
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
        viewModelScope.launch {
            refresh(sort = sort.value)
        }
    }

    fun onPaginate() {
        viewModelScope.launch {
            paginate(sort = sort.value)
        }
    }

    fun onSwitchSort(newSort: ArticleSort) {
        sort.value = newSort
    }

    fun onToggleVote(article: Article) {
        viewModelScope.launch {
            processingArticle.value = article
            withContext(Dispatchers.Default) {
                delay(500)
                repository.toggleVote(article)
            }
            processingArticle.value = null
        }
    }

}