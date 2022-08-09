package com.yuyakaido.gaia.account.infra

import com.yuyakaido.gaia.core.domain.Account
import com.yuyakaido.gaia.core.infra.ApiErrorHandler
import com.yuyakaido.gaia.core.infra.ApiExecutor
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
            when (val data = api.getUser(name).data) {
                is ObjectResponse.Data.AccountResponse -> data.toAccount()
                is ObjectResponse.Data.CommunityResponse -> throw RuntimeException()
            }
        }
    }

}