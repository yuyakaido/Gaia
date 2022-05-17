package com.yuyakaido.gaia.session

import com.yuyakaido.gaia.auth.Token
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("token") val token: Token
) {
    val bearerToken get() = "bearer ${token.accessToken}"
}