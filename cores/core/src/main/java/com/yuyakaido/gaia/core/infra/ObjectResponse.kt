package com.yuyakaido.gaia.core.infra

import android.net.Uri
import com.yuyakaido.gaia.core.domain.Account
import com.yuyakaido.gaia.core.domain.Community
import com.yuyakaido.gaia.core.domain.Kind
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Serializable
sealed class ObjectResponse {

    abstract val data: Data

    @Serializable
    sealed class Data {
        @Serializable
        data class AccountResponse(
            @SerialName("id") val id: String,
            @SerialName("name") val name: String,
            @SerialName("icon_img") val iconImg: String,
            @SerialName("created_utc") @Contextual val createdUtc: BigDecimal,
            @SerialName("total_karma") val totalKarma: Int,
            @SerialName("link_karma") val linkKarma: Int,
            @SerialName("comment_karma") val commentKarma: Int,
            @SerialName("awarder_karma") val awarderKarma: Int,
            @SerialName("awardee_karma") val awardeeKarma: Int
        ) : Data() {
            fun toAccount(): Account {
                return Account(
                    id = id,
                    name = name,
                    icon = Uri.parse(iconImg),
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

        @Serializable
        data class CommunityResponse(
            @SerialName("name") val name: String,
            @SerialName("display_name") val displayName: String,
            @SerialName("icon_img") val iconImg: String?,
            @SerialName("community_icon") val communityIcon: String?
        ) : Data() {
            fun toCommunity(): Community {
                return Community.Detail(
                    id = name.split("_").last(),
                    name = displayName,
                    icon = when {
                        iconImg?.isNotEmpty() == true -> {
                            Uri.parse(iconImg)
                        }
                        communityIcon?.isNotEmpty() == true -> {
                            Uri.parse(communityIcon).buildUpon().clearQuery().build()
                        }
                        else -> {
                            Uri.EMPTY
                        }
                    }
                )
            }
        }
    }

    @Serializable
    @SerialName(Kind.account)
    data class AccountElement(
        @SerialName("data") override val data: Data.AccountResponse
    ) : ObjectResponse()

    @Serializable
    @SerialName(Kind.community)
    data class CommunityElement(
        @SerialName("data") override val data: Data.CommunityResponse
    ) : ObjectResponse()

}