package com.yuyakaido.gaia.account.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.account.domain.AccountRepository
import com.yuyakaido.gaia.core.domain.Account
import com.yuyakaido.gaia.core.domain.Article
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
            val selectedTab: AccountTab,
            val posts: List<Article>
        ) : State()
    }

    private val args = AccountFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val account = args.name?.let {
        accountRepository.observeUser(it)
    } ?: accountRepository.observeMe()
    private val selectedTab = MutableStateFlow(AccountTab.Post)
    private val posts = MutableStateFlow<List<Article>>(emptyList())

    val state = combine(
        account,
        selectedTab,
        posts
    ) { account, selectedTab, posts ->
        State.Ideal(
            account = account,
            selectedTab = selectedTab,
            posts = posts
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = State.Loading
        )

    init {
        refreshAccount()
        refreshPosts()
    }

    private fun refreshAccount() {
        viewModelScope.launch {
            args.name?.let {
                accountRepository.refreshUser(it)
            } ?: accountRepository.refreshMe()
        }
    }

    private fun refreshPosts() {
        viewModelScope.launch {
            args.name?.let {
                accountRepository.getPosts(it)
                    .onSuccess { result -> posts.value = result.items }
            }
        }
    }

    fun onSelectTab(tab: AccountTab) {
        selectedTab.value = tab
    }

}