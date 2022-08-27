package com.yuyakaido.gaia.core.domain

sealed class Comment {
    abstract val id: String
    abstract val body: String

    data class Article(
        override val id: String,
        override val body: String,
        val author: Author
    ) : Comment()

    data class Account(
        override val id: String,
        override val body: String,
        val community: String,
        val article: String
    ) : Comment()
}