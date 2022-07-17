package com.yuyakaido.gaia.article.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.article.ArticleRepository
import com.yuyakaido.gaia.core.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    sealed class State {
        abstract val articles: List<Article>
        abstract val isLoading: Boolean
        object Initial : State() {
            override val isLoading: Boolean = false
            override val articles: List<Article> = emptyList()
        }
        data class Loading(
            override val articles: List<Article>
        ) : State() {
            override val isLoading: Boolean = true
        }
        data class Ideal(
            override val articles: List<Article>
        ) : State() {
            override val isLoading: Boolean = false
        }
    }

    private val articles = repository.observeArticles()
    private val isLoading = MutableStateFlow(false)

    val state = combine(
        articles, isLoading
    ) { articles, isLoading ->
        if (isLoading) {
            State.Loading(articles = articles)
        } else {
            State.Ideal(articles = articles)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State.Initial
    )

    init {
        refresh()
    }

    fun refresh() {
        paginate(refresh = true)
    }

    fun paginate(refresh: Boolean = false) {
        viewModelScope.launch {
            if (!isLoading.value) {
                isLoading.value = true
                repository.paginate(
                    after = articles.value.firstOrNull()?.id,
                    refresh = refresh
                )
                isLoading.value = false
            }
        }
    }

}