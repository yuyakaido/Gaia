package com.yuyakaido.gaia.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    data class State(
        val events: List<Event> = emptyList()
    )

    sealed class Event {
        object NavigateToMain : Event()
    }

    private val mutableStateFlow = MutableStateFlow(State())
    val state = mutableStateFlow.asLiveData()

    fun onCodeReceived(code: String) {
        viewModelScope.launch {
            authUseCase.authorize(code)
            mutableStateFlow.value = mutableStateFlow.value.copy(
                events = mutableStateFlow.value.events.plus(Event.NavigateToMain)
            )
        }
    }

    fun consume(event: Event) {
        mutableStateFlow.value = mutableStateFlow.value.copy(
            events = mutableStateFlow.value.events.minus(event)
        )
    }

}