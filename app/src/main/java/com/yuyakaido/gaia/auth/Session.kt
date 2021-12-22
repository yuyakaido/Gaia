package com.yuyakaido.gaia.auth

import android.app.Application
import android.content.Context
import androidx.core.content.edit

data class Session(
    val accessToken: String,
    val refreshToken: String?
) {
    val bearerToken get() = "bearer $accessToken"

    companion object {
        private const val SESSION = "session"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"

        fun get(application: Application): Session? {
            val preference = application.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            val accessToken = preference.getString(ACCESS_TOKEN, null)
            val refreshToken = preference.getString(REFRESH_TOKEN, null)
            return if (accessToken != null) {
                Session(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            } else {
                null
            }
        }

        fun put(application: Application, session: Session) {
            val preference = application.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            preference.edit(commit = false) {
                putString(ACCESS_TOKEN, session.accessToken)
                putString(REFRESH_TOKEN, session.refreshToken)
            }
        }
    }
}