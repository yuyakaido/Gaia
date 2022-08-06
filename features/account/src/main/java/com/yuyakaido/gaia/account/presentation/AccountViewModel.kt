package com.yuyakaido.gaia.account.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.account.domain.AccountRepository
import com.yuyakaido.gaia.core.domain.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository
) : ViewModel() {

    sealed class State {
        object Loading : State()
        data class Ideal(val account: Account) : State()
    }

    private val args = AccountFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val account = args.name?.let {
        accountRepository.observeUser(it)
    } ?: accountRepository.observeMe()
    val state = account
        .map { State.Ideal(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = State.Loading
        )

    init {
        viewModelScope.launch {
            args.name?.let {
                accountRepository.refreshUser(it)
            } ?: accountRepository.refreshMe()
        }
    }

}