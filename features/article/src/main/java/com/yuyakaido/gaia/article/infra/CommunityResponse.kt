package com.yuyakaido.gaia.article.infra

import android.net.Uri
import com.yuyakaido.gaia.core.domain.Community
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommunityResponse(
    @SerialName("kind") val kind: String,
    @SerialName("data") val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("display_name") val displayName: String,
        @SerialName("icon_img") val iconImg: String,
        @SerialName("community_icon") val communityIcon: String
    )

    fun toEntity(): Community {
        return Community.Detail(
            id = data.id,
            name = data.displayName,
            icon = when {
                data.iconImg.isNotEmpty() -> {
                    Uri.parse(data.iconImg)
                }
                data.communityIcon.isNotEmpty() -> {
                    Uri.parse(data.communityIcon).buildUpon().clearQuery().build()
                }
                else -> {
                    Uri.EMPTY
                }
            }
        )
    }
}