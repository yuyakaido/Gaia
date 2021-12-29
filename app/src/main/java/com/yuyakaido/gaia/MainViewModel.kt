package com.yuyakaido.gaia

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

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
                val articles = ArticleRepository.getPopularArticles(application)
                state.value = State.Ideal(articles)
            } catch (e: Exception) {
                state.value = State.Error
            }
        }
    }

}