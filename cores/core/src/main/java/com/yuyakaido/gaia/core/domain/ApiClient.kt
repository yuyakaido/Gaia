package com.yuyakaido.gaia.core.domain

import com.yuyakaido.gaia.core.infra.AccountApi
import com.yuyakaido.gaia.core.infra.ArticleApi
import com.yuyakaido.gaia.core.infra.MessageApi
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val accountApiProvider: Provider<AccountApi>,
    private val articleApiProvider: Provider<ArticleApi>,
    private val messageApiProvider: Provider<MessageApi>
) {

    private val accountApis = mutableMapOf<String, AccountApi>()
    private val articleApis = mutableMapOf<String, ArticleApi>()
    private val messageApis = mutableMapOf<String, MessageApi>()

    private suspend fun getActiveSession(): Session {
        return sessionRepository.getActiveSession()
            ?: throw RuntimeException("Active session is not specified.")
    }

    suspend fun getAccountApi(): AccountApi {
        return accountApis.getOrPut(getActiveSession().name) {
            accountApiProvider.get()
        }
    }

    suspend fun getArticleApi(): ArticleApi {
        return articleApis.getOrPut(getActiveSession().name) {
            articleApiProvider.get()
        }
    }

    suspend fun getMessageApi(): MessageApi {
        return messageApis.getOrPut(getActiveSession().name) {
            messageApiProvider.get()
        }
    }

}