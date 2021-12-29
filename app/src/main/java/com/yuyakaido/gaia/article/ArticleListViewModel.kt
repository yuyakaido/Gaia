package com.yuyakaido.gaia.article

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.domain.Article
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class ArticleListViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    sealed class State {
        object Initial : State()
        object Loading : State()
        object Error : State()
        data class Ideal(val articles: List<Article>) : State()
    }

    val state = mutableStateOf<State>(State.Initial)

    init {
        viewModelScope.launch {
            state.value = State.Loading
            try {
                val articles = repository.getPopularArticles()
                state.value = State.Ideal(articles)
            } catch (e: Exception) {
                state.value = State.Error
            }
        }
    }

}