package com.yuyakaido.gaia.message.infra

import com.yuyakaido.gaia.core.domain.Message
import com.yuyakaido.gaia.core.infra.ApiErrorHandler
import com.yuyakaido.gaia.core.infra.ApiExecutor
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MessageRemoteDataSource @Inject constructor(
    override val apiErrorHandler: ApiErrorHandler,
    private val api: MessageApi,
    private val json: Json
) : ApiExecutor {

    suspend fun getMessages(): Result<List<Message>> {
        return execute { api.getMessages().toMessages(json) }
    }

}