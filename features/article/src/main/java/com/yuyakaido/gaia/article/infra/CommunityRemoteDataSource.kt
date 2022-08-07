package com.yuyakaido.gaia.article.infra

import com.yuyakaido.gaia.core.domain.Community
import com.yuyakaido.gaia.core.infra.ApiErrorHandler
import com.yuyakaido.gaia.core.infra.ApiExecutor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRemoteDataSource @Inject constructor(
    override val apiErrorHandler: ApiErrorHandler,
    private val api: CommunityApi
) : ApiExecutor {

    suspend fun getCommunity(name: String): Result<Community> {
        return execute { api.getCommunity(name).toEntity() }
    }

}