package com.yuyakaido.gaia.core.domain

import android.net.Uri

data class Article(
    val id: ID,
    val title: String,
    val thumbnail: Uri,
    val community: Community,
    val author: Author,
    val likes: Boolean?,
    val ups: Int,
    val downs: Int,
    val numComments: Int
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
}