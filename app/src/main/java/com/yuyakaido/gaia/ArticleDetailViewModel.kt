package com.yuyakaido.gaia

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class ArticleDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    sealed class State {
        object Initial : State()
        data class Ideal(val article: Article) : State()
    }

    val state = mutableStateOf<State>(State.Initial)

    fun onCreate(id: String) {
        viewModelScope.launch {
            val article = ArticleRepository.getArticle(id = id)
            state.value = State.Ideal(article = article)
        }
    }

}