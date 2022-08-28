package com.yuyakaido.gaia.article.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.article.domain.ArticleRepository
import com.yuyakaido.gaia.core.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ArticleRepository
) : ViewModel() {

    sealed class State {
        object Loading : State()
        data class Ideal(
            val article: Article,
            val isProcessing: Boolean
        ) : State()
    }

    private val args = ArticleDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val article = repository.observeArticle(Article.ID(args.articleId))
    private val isProcessing = MutableStateFlow(false)

    val state = combine(
        article,
        isProcessing
    ) { article, isProcessing ->
        State.Ideal(
            article = article,
            isProcessing = isProcessing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State.Loading
    )

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
            if (currentState is State.Ideal && !isProcessing.value) {
                isProcessing.value = true
                withContext(Dispatchers.Default) {
                    delay(500)
                    repository.toggleVote(currentState.article)
                }
                isProcessing.value = false
            }
        }
    }

}