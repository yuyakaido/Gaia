package com.yuyakaido.gaia.core

import android.net.Uri
import android.webkit.URLUtil
import com.yuyakaido.gaia.domain.Article
import com.yuyakaido.gaia.domain.Kind
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://www.reddit.com/dev/api/
@Serializable
data class ListingDataResponse(
    @SerialName("data") val data: Children
) {
    @Serializable
    data class Children(
        @SerialName("children") val children: List<Child>,
        @SerialName("before") val before: String?,
        @SerialName("after") val after: String?
    ) {
        @Serializable
        sealed class Child {
            @Serializable
            sealed class Data {
                @Serializable
                data class Article(
                    @SerialName("id") val id: String,
                    @SerialName("title") val title: String,
                    @SerialName("thumbnail") val thumbnail: String?
                ) : Data() {
                    fun toThumbnailUri(): Uri {
                        return if (URLUtil.isNetworkUrl(thumbnail)) {
                            Uri.parse(thumbnail)
                        } else {
                            Uri.EMPTY
                        }
                    }
                }
            }
            @Serializable
            @SerialName(Kind.article)
            data class Article(
                @SerialName("data") override val data: Data.Article
            ) : Child() {
                fun toEntity(): com.yuyakaido.gaia.domain.Article {
                    return Article(
                        id = data.id,
                        title = data.title,
                        thumbnail = data.toThumbnailUri()
                    )
                }
            }
            abstract val data: Data
        }
    }
    fun toArticles(): List<Article> {
        return data
            .children
            .filterIsInstance<Children.Child.Article>()
            .map { it.toEntity() }
    }
}