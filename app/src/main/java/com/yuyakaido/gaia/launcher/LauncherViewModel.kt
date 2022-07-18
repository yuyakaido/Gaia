package com.yuyakaido.gaia.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.auth.domain.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    data class State(
        val events: List<Event> = emptyList()
    )

    sealed class Event {
        object NavigateToAuth : Event()
        object NavigateToMain : Event()
    }

    val state = MutableStateFlow(State())

    init {
        viewModelScope.launch {
            val session = sessionRepository.getActiveSession()
            if (session == null) {
                state.value = state.value.copy(
                    events = state.value.events.plus(Event.NavigateToAuth)
                )
            } else {
                state.value = state.value.copy(
                    events = state.value.events.plus(Event.NavigateToMain)
                )
            }
        }
    }

    fun consume(event: Event) {
        state.value = state.value.copy(
            events = state.value.events.minus(event)
        )
    }

}