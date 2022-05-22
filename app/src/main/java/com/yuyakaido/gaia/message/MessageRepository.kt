package com.yuyakaido.gaia.message

import com.yuyakaido.gaia.domain.Message
import com.yuyakaido.gaia.session.ApiClient
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val apiClient: ApiClient,
    private val json: Json
) {

    suspend fun getMessages(): List<Message> {
        return apiClient.getMessageApi().getMessages().toMessages(json)
    }

}