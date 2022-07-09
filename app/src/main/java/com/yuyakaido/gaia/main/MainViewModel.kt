package com.yuyakaido.gaia.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.core.domain.Session
import com.yuyakaido.gaia.core.domain.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    val sessions = mutableStateOf<List<Session>>(emptyList())

    init {
        viewModelScope.launch {
            sessions.value = sessionRepository.getAllSessions()
        }
    }

}