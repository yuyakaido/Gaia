package com.yuyakaido.gaia.message.domain

import com.yuyakaido.gaia.core.domain.Message
import com.yuyakaido.gaia.message.infra.MessageApi
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val api: MessageApi,
    private val json: Json
) {

    suspend fun getMessages(): List<Message> {
        return api.getMessages().toMessages(json)
    }

}