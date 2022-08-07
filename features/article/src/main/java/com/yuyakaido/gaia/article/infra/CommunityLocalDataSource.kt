package com.yuyakaido.gaia.article.infra

import com.yuyakaido.gaia.core.domain.Community
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityLocalDataSource @Inject constructor() {

    private val cache = MutableStateFlow<Map<String, Community>>(emptyMap())

    fun getCommunity(name: String): Community {
        return cache.value.getValue(name)
    }

    fun observeCommunity(name: String): Flow<Community> {
        return cache.mapNotNull { it[name] }
    }

    fun observeCommunities(names: List<String>): Flow<List<Community>> {
        return cache.map { communities -> names.mapNotNull { name -> communities[name] } }
    }

    fun emitCommunity(community: Community) {
        emitCommunities(listOf(community))
    }

    fun emitCommunities(communities: List<Community>) {
        cache.value = cache.value.plus(communities.map { it.name to it })
    }

}