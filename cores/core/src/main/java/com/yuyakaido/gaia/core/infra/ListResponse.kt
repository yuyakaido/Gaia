package com.yuyakaido.gaia.core.infra

import android.net.Uri
import android.webkit.URLUtil
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.domain.Kind
import com.yuyakaido.gaia.core.domain.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

// https://www.reddit.com/dev/api/
@Serializable
data class ListResponse(
    @SerialName("data") val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("children") val children: List<Child>,
        @SerialName("before") val before: String?,
        @SerialName("after") val after: String?
    ) {
        @Serializable
        sealed class Child {
            @Serializable
            sealed class Data {
                @Serializable
                data class ArticleResponse(
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
                @Serializable
                data class MessageResponse(
                    @SerialName("id") val id: String,
                    @SerialName("author") val author: String,
                    @SerialName("subject") val subject: String,
                    @SerialName("body") val body: String,
                    @SerialName("replies") val replies: JsonElement
                ) : Data()
            }
            @Serializable
            @SerialName(Kind.article)
            data class ArticleElement(
                @SerialName("data") override val data: Data.ArticleResponse
            ) : Child() {
                fun toArticle(): Article {
                    return Article(
                        id = Article.ID(data.id),
                        title = data.title,
                        thumbnail = data.toThumbnailUri()
                    )
                }
            }
            @Serializable
            @SerialName(Kind.message)
            data class MessageElement(
                @SerialName("data") override val data: Data.MessageResponse
            ) : Child() {
                fun toMessage(): Message {
                    return Message(
                        id = data.id,
                        author = data.author,
                        subject = data.subject,
                        body = data.body
                    )
                }
            }
            abstract val data: Data
        }
    }
    fun toArticles(): ListingResult<Article> {
        return ListingResult(
            items = data
                .children
                .filterIsInstance<Data.Child.ArticleElement>()
                .map { it.toArticle() },
            before = data.before,
            after = data.after
        )
    }
    fun toMessages(json: Json): List<Message> {
        return data
            .children
            .filterIsInstance<Data.Child.MessageElement>()
            .map {
                val replies = it.data.replies
                if (replies is JsonObject) {
                    val response = json.decodeFromJsonElement<ListResponse>(replies)
                    response.toMessages(json).last()
                } else {
                    it.toMessage()
                }
            }
    }
}
