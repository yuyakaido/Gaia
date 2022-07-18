package com.yuyakaido.gaia.account.infra

import android.net.Uri
import com.yuyakaido.gaia.account.domain.Account
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Serializable
data class AccountResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("icon_img") val iconImg: String,
    @SerialName("total_karma") val totalKarma: Int,
    @SerialName("created_utc") @Contextual val createdUtc: BigDecimal
) {
    fun toEntity(): Account {
        return Account(
            id = id,
            name = name,
            icon = Uri.parse(iconImg),
            totalKarma = totalKarma,
            createdAt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(createdUtc.longValueExact()),
                ZoneId.systemDefault()
            )
        )
    }
}