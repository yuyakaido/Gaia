package com.yuyakaido.gaia.account.domain

import com.yuyakaido.gaia.account.infra.AccountApi
import com.yuyakaido.gaia.core.domain.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val api: AccountApi
) {

    private val me = MutableStateFlow<Account?>(null)
    private val users = MutableStateFlow<Map<String, Account>>(emptyMap())

    fun observeMe(): Flow<Account> {
        return me.filterNotNull()
    }

    fun observeUser(name: String): Flow<Account> {
        return users.mapNotNull { it[name] }
    }

    suspend fun refreshMe(): Account {
        val response = api.getMe().toEntity()
        me.value = response
        return response
    }

    suspend fun refreshUser(name: String): Account {
        val response = api.getUser(name).toAccount()
        users.value = users.value.plus(name to response)
        return response
    }

}