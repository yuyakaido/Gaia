package com.yuyakaido.gaia.account.domain

import com.yuyakaido.gaia.account.infra.AccountRemoteDataSource
import com.yuyakaido.gaia.core.domain.Account
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.domain.Comment
import com.yuyakaido.gaia.core.infra.ListingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val remote: AccountRemoteDataSource
) {

    private val me = MutableStateFlow<Account?>(null)
    private val users = MutableStateFlow<Map<String, Account>>(emptyMap())

    fun observeMe(): Flow<Account> {
        return me.filterNotNull()
    }

    fun observeUser(name: String): Flow<Account> {
        return users.mapNotNull { it[name] }
    }

    suspend fun refreshMe(): Result<Account> {
        return remote.getMe()
            .onSuccess { me.value = it }
    }

    suspend fun refreshUser(name: String): Result<Account> {
        return remote.getUser(name)
            .onSuccess { users.value = users.value.plus(it.name to it) }
    }

    suspend fun getPosts(name: String): Result<ListingResult<Article>> {
        return remote.getPosts(name)
    }

    suspend fun getComments(name: String): Result<ListingResult<Comment>> {
        return remote.getComments(name)
    }

}