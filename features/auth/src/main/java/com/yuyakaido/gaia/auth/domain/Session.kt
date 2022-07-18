package com.yuyakaido.gaia.auth.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("token") val token: Token,
    @SerialName("is_active") val isActive: Boolean
) {
    val bearerToken get() = "bearer ${token.accessToken}"
}