package com.yuyakaido.gaia.core.domain

import android.net.Uri

data class Article(
    val id: ID,
    val title: String,
    val body: String?,
    val thumbnail: Uri,
    val community: Community,
    val author: Author,
    val likes: Boolean?,
    val ups: Int,
    val downs: Int,
    val numComments: Int,
    val comments: List<Comment.Article>,
    val url: Uri
) {
    data class ID(val value: String) {
        fun full(): String {
            return "${Kind.article}_$value"
        }
    }

    val reactions = ups + downs

    fun toVoted(): Article {
        return copy(likes = true, ups = ups.inc())
    }

    fun toUnvoted(): Article {
        return copy(likes = null, ups = ups.dec())
    }

    fun flattenedComments(): List<Comment.Article> {
        fun collectAllReplies(
            depth: Int,
            comment: Comment.Article
        ): List<Comment.Article> {
            return if (comment.replies.isEmpty()) {
                listOf(comment.copy(depth = depth))
            } else {
                listOf(comment.copy(depth = depth))
                    .plus(
                        comment.replies.flatMap {
                            collectAllReplies(
                                depth = depth.inc(),
                                comment = it
                            )
                        }
                    )
                }
            }
        return comments.flatMap {
            collectAllReplies(depth = it.depth, comment = it)
        }
    }
}