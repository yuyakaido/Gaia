package com.yuyakaido.gaia.account

import android.net.Uri
import com.yuyakaido.gaia.domain.Account
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("icon_img") val iconImg: String
) {
    fun toEntity(): Account {
        return Account(
            id = id,
            name = name,
            icon = Uri.parse(iconImg)
        )
    }
}