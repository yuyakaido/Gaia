package com.yuyakaido.gaia.core.domain

data class Author(
    val id: String,
    val name: String
) {
    fun display(): String {
        return "u/$name"
    }
    fun full(): String {
        return "${Kind.account}_$id"
    }
}