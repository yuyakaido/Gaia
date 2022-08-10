package com.yuyakaido.gaia.account.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.account.domain.AccountRepository
import com.yuyakaido.gaia.core.domain.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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
        data class Ideal(
            val account: Account,
            val selectedTab: AccountTab
        ) : State()
    }

    private val args = AccountFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val account = args.name?.let {
        accountRepository.observeUser(it)
    } ?: accountRepository.observeMe()
    private val selectedTab = MutableStateFlow(AccountTab.Post)
    val state = combine(
        account, selectedTab
    ) { account, selectedTab ->
        State.Ideal(
            account = account,
            selectedTab = selectedTab
        )
    }
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

    fun onSelectTab(tab: AccountTab) {
        selectedTab.value = tab
    }

}