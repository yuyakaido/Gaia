package com.yuyakaido.gaia.core.infra

interface ApiExecutor {
    val apiErrorHandler: ApiErrorHandler

    suspend fun <T> execute(block: suspend () -> T): Result<T> {
        return runCatching { block.invoke() }
            .onFailure { apiErrorHandler.handle(it) }
    }
}