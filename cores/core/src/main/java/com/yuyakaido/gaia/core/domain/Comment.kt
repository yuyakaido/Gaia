package com.yuyakaido.gaia.core.domain

data class Comment(
    val id: String,
    val body: String,
    val community: Community,
    val post: Post
) {
    data class Community(
        val name: String
    )
    data class Post(
        val title: String
    )
}