package com.yuyakaido.gaia

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
                    @SerialName("name") val name: String,
                    @SerialName("subreddit") val community: String,
                    @SerialName("title") val title: String,
                    @SerialName("thumbnail") val thumbnail: String?,
                    @SerialName("author") val author: String,
                    @SerialName("likes") val likes: Boolean?,
                    @SerialName("ups") val ups: Int,
                    @SerialName("downs") val downs: Int,
                    @SerialName("num_comments") val comments: Int
                ) : Data()
            }
            @Serializable
            @SerialName(Kind.article)
            data class Article(
                @SerialName("data") override val data: Data.Article
            ) : Child() {
                fun toEntity(): com.yuyakaido.gaia.Article {
                    return Article(
                        title = data.title
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
