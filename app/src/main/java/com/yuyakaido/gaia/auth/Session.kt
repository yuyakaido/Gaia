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
    @SerialName("name") val name: String,
    @SerialName("token") val token: Token
) {
    val bearerToken get() = "bearer ${token.accessToken}"

    companion object {
        private const val SESSIONS = "sessions"
        private const val ACTIVE_SESSIONS = "active_sessions"
        private const val ACTIVE_SESSION_INDEX = "active_session_index"

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
            val preference = application.getSharedPreferences(SESSIONS, Context.MODE_PRIVATE)
            val activeSessionIndex = preference.getInt(ACTIVE_SESSION_INDEX, 0)
            return all(application).getOrNull(activeSessionIndex)
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
            }.distinctBy { it.name }
            val newActiveSessionIndex = newActiveSessions.indexOfFirst { it.name == session.name }

            val json = buildJsonArray {
                newActiveSessions.forEach {
                    add(Json.encodeToJsonElement(it))
                }
            }.toString()

            val preference = application.getSharedPreferences(SESSIONS, Context.MODE_PRIVATE)
            preference.edit(commit = false) {
                putString(ACTIVE_SESSIONS, json)
                putInt(ACTIVE_SESSION_INDEX, newActiveSessionIndex)
            }
        }

        fun activate(application: Application, session: Session) {
            val sessions = all(application = application)
            val activeSessionIndex = sessions.indexOf(session)

            val preference = application.getSharedPreferences(SESSIONS, Context.MODE_PRIVATE)
            preference.edit(commit = false) {
                putInt(ACTIVE_SESSION_INDEX, activeSessionIndex)
            }
        }
    }
}