package com.yuyakaido.gaia.message.domain

import com.yuyakaido.gaia.core.domain.Message
import com.yuyakaido.gaia.message.infra.MessageRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val remote: MessageRemoteDataSource
) {

    suspend fun getMessages(): Result<List<Message>> {
        return remote.getMessages()
    }

}