package com.yuyakaido.gaia

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class ArticleDetailViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    sealed class State {
        object Initial : State()
        data class Ideal(val article: Article) : State()
    }

    val state = mutableStateOf<State>(State.Initial)

    fun onCreate(id: String) {
        viewModelScope.launch {
            val article = repository.getArticle(id = id)
            state.value = State.Ideal(article = article)
        }
    }

}