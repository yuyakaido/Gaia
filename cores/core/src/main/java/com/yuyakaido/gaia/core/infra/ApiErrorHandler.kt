package com.yuyakaido.gaia.core.infra

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiErrorHandler @Inject constructor() {

    fun handle(throwable: Throwable): Throwable {
        Timber.e(throwable)
        return throwable
    }

}