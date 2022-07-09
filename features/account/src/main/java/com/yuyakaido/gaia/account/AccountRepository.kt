package com.yuyakaido.gaia.account

import com.yuyakaido.gaia.core.domain.Account
import com.yuyakaido.gaia.core.domain.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    private val account = MutableStateFlow<Account?>(null)

    fun observeMe(): Flow<Account> {
        return account.filterNotNull()
    }

    suspend fun refreshMe(): Account {
        val me = apiClient.getAccountApi().getMe().toEntity()
        account.emit(me)
        return me
    }

}