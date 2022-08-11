package com.yuyakaido.gaia.account.infra

import androidx.core.net.toUri
import com.yuyakaido.gaia.core.domain.Account
import com.yuyakaido.gaia.core.extension.toUriWithoutQuery
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Serializable
data class MeResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("icon_img") val iconImg: String,
    @SerialName("created_utc") @Contextual val createdUtc: BigDecimal,
    @SerialName("total_karma") val totalKarma: Int,
    @SerialName("link_karma") val linkKarma: Int,
    @SerialName("comment_karma") val commentKarma: Int,
    @SerialName("awarder_karma") val awarderKarma: Int,
    @SerialName("awardee_karma") val awardeeKarma: Int
) {
    fun toAccount(): Account {
        return Account(
            id = id,
            name = name,
            icon = iconImg.toUriWithoutQuery(),
            createdAt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(createdUtc.longValueExact()),
                ZoneId.systemDefault()
            ),
            totalKarma = totalKarma,
            postKarma = linkKarma,
            commentKarma = commentKarma,
            awarderKarma = awarderKarma,
            awardeeKarma = awardeeKarma
        )
    }
}