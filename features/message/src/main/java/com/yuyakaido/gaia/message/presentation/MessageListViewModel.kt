package com.yuyakaido.gaia.message.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.core.domain.Message
import com.yuyakaido.gaia.message.domain.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
                repository.getMessages()
                    .onSuccess { state.value = State.Ideal(it) }
            } catch (e: Exception) {
                state.value = State.Error
            }
        }
    }

}