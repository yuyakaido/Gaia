package com.yuyakaido.gaia.core.domain

data class Message(
    val id: String,
    val author: String,
    val subject: String,
    val body: String
)