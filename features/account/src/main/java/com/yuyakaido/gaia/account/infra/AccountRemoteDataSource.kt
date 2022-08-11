package com.yuyakaido.gaia.account.infra

import com.yuyakaido.gaia.core.domain.Account
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.domain.Comment
import com.yuyakaido.gaia.core.domain.Trophy
import com.yuyakaido.gaia.core.infra.ApiErrorHandler
import com.yuyakaido.gaia.core.infra.ApiExecutor
import com.yuyakaido.gaia.core.infra.ListingResult
import com.yuyakaido.gaia.core.infra.ObjectResponse
import javax.inject.Inject

class AccountRemoteDataSource @Inject constructor(
    override val apiErrorHandler: ApiErrorHandler,
    private val api: AccountApi
) : ApiExecutor {

    suspend fun getMe(): Result<Account> {
        return execute { api.getMe().toAccount() }
    }

    suspend fun getUser(name: String): Result<Account> {
        return execute {
            when (val response = api.getUser(name)) {
                is ObjectResponse.AccountElement -> response.data.toAccount()
                is ObjectResponse.CommunityElement -> throw RuntimeException()
            }
        }
    }

    suspend fun getPosts(name: String): Result<ListingResult<Article>> {
        return execute { api.getPosts(name).toArticles() }
    }

    suspend fun getComments(name: String): Result<ListingResult<Comment>> {
        return execute { api.getComments(name).toComments() }
    }

    suspend fun getTrophies(name: String): Result<List<Trophy>> {
        return execute { api.getTrophies(name).toTrophies() }
    }

}