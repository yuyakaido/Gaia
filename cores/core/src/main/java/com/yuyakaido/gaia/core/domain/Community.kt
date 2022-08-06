package com.yuyakaido.gaia.core.domain

data class Community(
    val id: String,
    val name: String
) {
    fun display(): String {
        return "r/$name"
    }
    fun full(): String {
        return "${Kind.community}_$id"
    }
}