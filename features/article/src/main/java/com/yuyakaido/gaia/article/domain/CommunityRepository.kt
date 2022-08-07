package com.yuyakaido.gaia.article.domain

import com.yuyakaido.gaia.article.infra.CommunityLocalDataSource
import com.yuyakaido.gaia.article.infra.CommunityRemoteDataSource
import com.yuyakaido.gaia.core.domain.Community
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepository @Inject constructor(
    private val remote: CommunityRemoteDataSource,
    private val local: CommunityLocalDataSource
) {

    fun getCommunity(name: String): Community {
        return local.getCommunity(name)
    }

    suspend fun refreshCommunity(name: String): Result<Community> {
        return remote.getCommunity(name)
            .onSuccess { local.emitCommunity(it) }
    }

    suspend fun refreshCommunities(names: List<String>): Result<List<Community>> {
        val communities = names.mapNotNull { remote.getCommunity(it).getOrNull() }
        local.emitCommunities(communities)
        return Result.success(communities)
    }

    fun observeCommunity(name: String): Flow<Community> {
        return local.observeCommunity(name)
    }

    fun observeCommunities(names: List<String>): Flow<List<Community>> {
        return local.observeCommunities(names)
    }

}