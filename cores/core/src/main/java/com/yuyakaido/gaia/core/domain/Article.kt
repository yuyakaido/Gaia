package com.yuyakaido.gaia.core.domain

import android.net.Uri

data class Article(
    val id: ID,
    val title: String,
    val thumbnail: Uri,
    val likes: Boolean?,
    val ups: Int,
    val downs: Int,
    val numComments: Int
) {
    data class ID(val value: String) {
        fun forPagination(): String {
            return "${Kind.article}_$value"
        }
    }

    val reactions = ups + downs

    fun toggleVote(): Article {
        return when (likes) {
            null -> copy(likes = true)
            false -> copy(likes = true)
            true -> copy(likes = false)
        }
    }
}