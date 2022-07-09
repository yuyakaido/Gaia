package com.yuyakaido.gaia.message

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.core.domain.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor(
    repository: MessageRepository
) : ViewModel() {

    sealed class State {
        object Initial : State()
        object Loading : State()
        object Error : State()
        data class Ideal(val messages: List<Message>) : State()
    }

    val state = mutableStateOf<State>(State.Initial)

    init {
        viewModelScope.launch {
            state.value = State.Loading
            try {
                val messages = repository.getMessages()
                state.value = State.Ideal(messages = messages)
            } catch (e: Exception) {
                state.value = State.Error
                Timber.e(e)
            }
        }
    }

}