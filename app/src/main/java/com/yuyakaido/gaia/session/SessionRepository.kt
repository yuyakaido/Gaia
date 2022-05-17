package com.yuyakaido.gaia.session

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val application: Application
) {

    companion object {
        private const val SESSIONS = "sessions"
        private const val ACTIVE_SESSIONS = "active_sessions"
        private const val ACTIVE_SESSION_INDEX = "active_session_index"
    }

    suspend fun getAllSessions(): List<Session> {
        return withContext(Dispatchers.IO) {
            val preference = application.getSharedPreferences(SESSIONS, Context.MODE_PRIVATE)
            val json = preference.getString(ACTIVE_SESSIONS, null)
            if (json == null) {
                emptyList()
            } else {
                Json.decodeFromString<JsonArray>(json)
                    .map { Json.decodeFromJsonElement(it) }
            }
        }
    }

    suspend fun getActiveSession(): Session? {
        return withContext(Dispatchers.IO) {
            val preference = application.getSharedPreferences(SESSIONS, Context.MODE_PRIVATE)
            val activeSessionIndex = preference.getInt(ACTIVE_SESSION_INDEX, 0)
            getAllSessions().getOrNull(activeSessionIndex)
        }
    }

    suspend fun putSession(session: Session) {
        withContext(Dispatchers.IO) {
            val currentActiveSessions = getAllSessions()
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
    }

    suspend fun activateSession(session: Session) {
        withContext(Dispatchers.IO) {
            val sessions = getAllSessions()
            val activeSessionIndex = sessions.indexOf(session)

            val preference = application.getSharedPreferences(SESSIONS, Context.MODE_PRIVATE)
            preference.edit(commit = false) {
                putInt(ACTIVE_SESSION_INDEX, activeSessionIndex)
            }
        }
    }

}