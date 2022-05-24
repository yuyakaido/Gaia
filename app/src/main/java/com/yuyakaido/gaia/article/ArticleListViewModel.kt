package com.yuyakaido.gaia.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
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