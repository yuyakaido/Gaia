package com.yuyakaido.gaia.account.domain

import com.yuyakaido.gaia.account.infra.AccountApi
import com.yuyakaido.gaia.core.domain.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val api: AccountApi
) {

    private val account = MutableStateFlow<Account?>(null)

    fun observeMe(): Flow<Account> {
        return account.filterNotNull()
    }

    suspend fun refreshMe(): Account {
        val me = api.getMe().toEntity()
        account.emit(me)
        return me
    }

}