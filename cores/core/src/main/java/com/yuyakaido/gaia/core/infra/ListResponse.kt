package com.yuyakaido.gaia.core.infra

import com.yuyakaido.gaia.core.domain.*
import com.yuyakaido.gaia.core.extension.toUriWithoutQuery
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
        @SerialName("children") val children: List<Child>? = emptyList(),
        @SerialName("trophies") val trophies: List<Child>? = emptyList(),
        @SerialName("before") val before: String? = null,
        @SerialName("after") val after: String? = null
    ) {

        @Serializable
        sealed class Child {
            abstract val data: Data

            @Serializable
            sealed class Data {
                @Serializable
                data class CommentResponse(
                    @SerialName("id") val id: String,
                    @SerialName("body") val body: String,
                    @SerialName("subreddit") val subreddit: String,
                    @SerialName("link_title") val linkTitle: String
                ) : Data()

                @Serializable
                data class ArticleResponse(
                    @SerialName("id") val id: String,
                    @SerialName("title") val title: String,
                    @SerialName("selftext") val selftext: String?,
                    @SerialName("thumbnail") val thumbnail: String?,
                    @SerialName("sr_detail") val srDetail: ObjectResponse.Data.CommunityResponse,
                    @SerialName("author_fullname") val authorFullname: String,
                    @SerialName("author") val author: String,
                    @SerialName("likes") val likes: Boolean?,
                    @SerialName("ups") val ups: Int,
                    @SerialName("downs") val downs: Int,
                    @SerialName("num_comments") val numComments: Int
                ) : Data()

                @Serializable
                data class MessageResponse(
                    @SerialName("id") val id: String,
                    @SerialName("author") val author: String,
                    @SerialName("subject") val subject: String,
                    @SerialName("body") val body: String,
                    @SerialName("replies") val replies: JsonElement
                ) : Data()

                @Serializable
                data class AwardResponse(
                    @SerialName("name") val name: String,
                    @SerialName("icon_70") val icon70: String
                ) : Data()
            }

            @Serializable
            @SerialName(Kind.comment)
            data class CommentElement(
                @SerialName("data") override val data: Data.CommentResponse
            ) : Child() {
                fun toComment(): Comment {
                    return Comment(
                        id = data.id,
                        body = data.body,
                        community = Comment.Community(
                            name = data.subreddit
                        ),
                        post = Comment.Post(
                            title = data.linkTitle
                        )
                    )
                }
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
                        body = data.selftext,
                        thumbnail = data.thumbnail.toUriWithoutQuery(),
                        community = data.srDetail.toCommunity(),
                        author = Author(
                            id = data.authorFullname,
                            name = data.author
                        ),
                        likes = data.likes,
                        ups = data.ups,
                        downs = data.downs,
                        numComments = data.numComments
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

            @Serializable
            @SerialName(Kind.award)
            data class AwardElement(
                @SerialName("data") override val data: Data.AwardResponse
            ) : Child() {
                fun toTrophy(): Trophy {
                    return Trophy(
                        name = data.name,
                        icon = data.icon70.toUriWithoutQuery()
                    )
                }
            }

        }

    }

    fun toComments(): ListingResult<Comment> {
        return ListingResult(
            items = data.children
                ?.filterIsInstance<Data.Child.CommentElement>()
                ?.map { it.toComment() }
                ?: emptyList(),
            before = data.before,
            after = data.after
        )
    }

    fun toArticles(): ListingResult<Article> {
        return ListingResult(
            items = data.children
                ?.filterIsInstance<Data.Child.ArticleElement>()
                ?.map { it.toArticle() }
                ?: emptyList(),
            before = data.before,
            after = data.after
        )
    }

    fun toMessages(json: Json): List<Message> {
        return data.children
            ?.filterIsInstance<Data.Child.MessageElement>()
            ?.map {
                val replies = it.data.replies
                if (replies is JsonObject) {
                    val response = json.decodeFromJsonElement<ListResponse>(replies)
                    response.toMessages(json).last()
                } else {
                    it.toMessage()
                }
            }
            ?: emptyList()
    }

    fun toTrophies(): List<Trophy> {
        return data.trophies
            ?.filterIsInstance<Data.Child.AwardElement>()
            ?.map { it.toTrophy() }
            ?: emptyList()
    }

}
