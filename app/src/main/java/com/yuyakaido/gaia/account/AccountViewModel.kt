package com.yuyakaido.gaia.account

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuyakaido.gaia.domain.Account
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    sealed class State {
        object Initial : State()
        object Loading : State()
        object Error : State()
        data class Ideal(val account: Account) : State()
    }

    val state = mutableStateOf<State>(State.Initial)

    init {
        viewModelScope.launch {
            state.value = State.Loading
            try {
                val account = accountRepository.getMe()
                state.value = State.Ideal(account = account)
            } catch (e: Exception) {
                state.value = State.Error
            }
        }
    }

}