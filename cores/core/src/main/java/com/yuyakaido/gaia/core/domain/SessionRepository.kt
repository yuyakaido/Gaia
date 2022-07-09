package com.yuyakaido.gaia.core.domain

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val application: Application
) {

    companion object {
        private const val SHARED_PREFERENCES_SESSIONS = "shared_preferences_sessions"
        private const val KEY_SESSIONS_JSON = "sessions_json"
    }

    private val preference by lazy {
        application.getSharedPreferences(SHARED_PREFERENCES_SESSIONS, Context.MODE_PRIVATE)
    }

    suspend fun getAllSessions(): List<Session> {
        return withContext(Dispatchers.IO) {
            val json = preference.getString(KEY_SESSIONS_JSON, null)
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
            getAllSessions().firstOrNull { it.isActive }
        }
    }

    suspend fun createSession(token: Token): Session {
        return Session(
            id = UUID.randomUUID().toString(),
            name = "",
            token = token,
            isActive = true
        ).also { putSession(it) }
    }

    suspend fun putSession(session: Session) {
        withContext(Dispatchers.IO) {
            val currentSessions = getAllSessions().let { sessions ->
                if (session.isActive) {
                    sessions.map { it.copy(isActive = false) }
                } else {
                    sessions
                }
            }
            val existsSession = currentSessions.any { it.id == session.id }
            val newSessions = if (existsSession) {
                currentSessions.map {
                    if (it.id == session.id) {
                        session
                    } else {
                        it
                    }
                }
            } else {
                listOf(session).plus(currentSessions)
            }.distinctBy { it.name }

            val json = buildJsonArray {
                newSessions.forEach {
                    add(Json.encodeToJsonElement(it))
                }
            }.toString()

            preference.edit(commit = false) {
                putString(KEY_SESSIONS_JSON, json)
            }
        }
    }

    suspend fun activateSession(session: Session) {
        withContext(Dispatchers.IO) {
            putSession(session.copy(isActive = true))
        }
    }

}