package com.yuyakaido.gaia.account

import com.yuyakaido.gaia.domain.Account
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountApi: AccountApi
) {

   suspend fun getMe(): Account {
       return accountApi.getMe().toEntity()
    }

}