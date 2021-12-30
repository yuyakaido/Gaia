package com.yuyakaido.gaia.auth

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

@Serializable
data class Session(
    @SerialName("id") val id: String,
    @SerialName("token") val token: Token
) {
    val bearerToken get() = "bearer ${token.accessToken}"

    companion object {
        private const val SESSIONS = "sessions"
        private const val ACTIVE_SESSIONS = "active_sessions"

        fun all(application: Application): List<Session> {
            val preference = application.getSharedPreferences(SESSIONS, Context.MODE_PRIVATE)
            val json = preference.getString(ACTIVE_SESSIONS, null)
            return if (json == null) {
                emptyList()
            } else {
                Json.decodeFromString<JsonArray>(json)
                    .map { Json.decodeFromJsonElement(it) }
            }
        }

        fun get(application: Application): Session? {
            return all(application).firstOrNull()
        }

        fun put(application: Application, session: Session) {
            val currentActiveSessions = all(application)
            val existsActiveSession = currentActiveSessions.any { it.id == session.id }
            val newActiveSessions = if (existsActiveSession) {
                currentActiveSessions.map {
                    if (it.id == session.id) {
                        session
                    } else {
                        it
                    }
                }
            } else {
                currentActiveSessions.plus(session)
            }

            val json = buildJsonArray {
                newActiveSessions.forEach {
                    add(Json.encodeToJsonElement(it))
                }
            }.toString()

            val preference = application.getSharedPreferences(SESSIONS, Context.MODE_PRIVATE)
            preference.edit(commit = false) {
                putString(ACTIVE_SESSIONS, json)
            }
        }
    }
}