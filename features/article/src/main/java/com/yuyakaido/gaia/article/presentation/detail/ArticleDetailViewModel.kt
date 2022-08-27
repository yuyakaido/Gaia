package com.yuyakaido.gaia.article.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.article.domain.ArticleRepository
import com.yuyakaido.gaia.core.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ArticleRepository
) : ViewModel() {

    sealed class State {
        object Loading : State()
        data class Ideal(val article: Article) : State()
    }

    private val args = ArticleDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)
    val state = repository.observeArticle(Article.ID(args.articleId))
        .map { State.Ideal(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = State.Loading
        )
    val article = state.mapNotNull {
        if (it is State.Ideal) {
            it.article
        } else {
            null
        }
    }

    init {
        fetchComments()
    }

    private fun fetchComments() {
        viewModelScope.launch {
            article.filterNotNull()
                .take(1)
                .collectLatest {
                    repository.getComments(it)
                }
        }
    }

    fun onToggleVote() {
        viewModelScope.launch {
            val currentState = state.value
            if (currentState is State.Ideal) {
                repository.toggleVote(currentState.article)
            }
        }
    }

}