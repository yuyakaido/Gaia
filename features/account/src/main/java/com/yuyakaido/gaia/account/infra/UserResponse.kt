package com.yuyakaido.gaia.account.infra

import android.net.Uri
import com.yuyakaido.gaia.core.domain.Account
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Serializable
data class UserResponse(
    @SerialName("kind") val kind: String,
    @SerialName("data") val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("name") val name: String,
        @SerialName("icon_img") val iconImg: String,
        @SerialName("total_karma") val totalKarma: Int,
        @SerialName("created_utc") @Contextual val createdUtc: BigDecimal
    )

    fun toAccount(): Account {
        return Account(
            id = data.id,
            name = data.name,
            icon = Uri.parse(data.iconImg),
            totalKarma = data.totalKarma,
            createdAt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(data.createdUtc.longValueExact()),
                ZoneId.systemDefault()
            )
        )
    }
}
