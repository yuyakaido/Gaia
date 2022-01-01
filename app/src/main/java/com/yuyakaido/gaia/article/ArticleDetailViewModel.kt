package com.yuyakaido.gaia.article

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yuyakaido.gaia.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ArticleRepository
) : ViewModel() {

    sealed class State {
        object Initial : State()
        data class Ideal(val article: Article) : State()
    }

    val state = mutableStateOf<State>(State.Initial)

    init {
        val id = savedStateHandle.get<String>("id")
        id?.let {
            val article = repository.getArticle(id = id)
            state.value = State.Ideal(article = article)
        }
    }

}