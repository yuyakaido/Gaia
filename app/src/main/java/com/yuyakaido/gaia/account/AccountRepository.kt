package com.yuyakaido.gaia.account

import com.yuyakaido.gaia.domain.Account
import com.yuyakaido.gaia.session.ApiClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val apiClient: ApiClient
) {

   suspend fun getMe(): Account {
       return apiClient.getAccountApi().getMe().toEntity()
    }

}