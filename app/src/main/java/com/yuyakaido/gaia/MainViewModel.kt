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

    val state = mutableStateOf<State>(State.Initial)

    sealed class State {
        object Initial : State()
        object Loading : State()
        object Error : State()
        data class Ideal(val articles: List<Article>) : State()
    }

    init {
        viewModelScope.launch {
            state.value = State.Loading
            val api = Networking.createArticleApi(application)
            try {
                val articles = api.getPopularArticles().toArticles()
                state.value = State.Ideal(articles)
            } catch (e: Exception) {
                state.value = State.Error
            }
        }
    }

}