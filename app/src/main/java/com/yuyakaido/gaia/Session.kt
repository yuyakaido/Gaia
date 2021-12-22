package com.yuyakaido.gaia

import android.app.Activity
import android.content.Context
import androidx.core.content.edit

data class Session(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"

        fun get(activity: Activity): Session? {
            val preference = activity.getPreferences(Context.MODE_PRIVATE)
            val accessToken = preference.getString(ACCESS_TOKEN, null)
            val refreshToken = preference.getString(REFRESH_TOKEN, null)
            return if (accessToken != null && refreshToken != null) {
                Session(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            } else {
                null
            }
        }

        fun put(activity: Activity, session: Session) {
            val preference = activity.getPreferences(Context.MODE_PRIVATE)
            preference.edit(commit = false) {
                putString(ACCESS_TOKEN, session.accessToken)
                putString(REFRESH_TOKEN, session.refreshToken)
            }
        }
    }
}