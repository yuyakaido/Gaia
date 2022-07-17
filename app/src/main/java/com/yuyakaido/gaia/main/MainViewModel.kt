package com.yuyakaido.gaia.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.core.domain.Session
import com.yuyakaido.gaia.core.domain.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    data class State(
        val sessions: List<Session> = emptyList(),
        val events: List<Event> = emptyList()
    )

    sealed class Event {
        object NavigateToAuth : Event()
        object Relaunch : Event()
    }

    val state = MutableStateFlow(State())

    init {
        viewModelScope.launch {
            state.value = state.value.copy(sessions = sessionRepository.getAllSessions())
        }
    }

    fun onAddNewSession() {
        state.value = state.value.copy(
            events = state.value.events.plus(Event.NavigateToAuth)
        )
    }

    fun onActivateSession(session: Session) {
        viewModelScope.launch {
            sessionRepository.activateSession(session)
            state.value = state.value.copy(
                events = state.value.events.plus(Event.Relaunch)
            )
        }
    }

    fun onConsumeEvent(event: Event) {
        state.value = state.value.copy(
            events = state.value.events.minus(event)
        )
    }

}