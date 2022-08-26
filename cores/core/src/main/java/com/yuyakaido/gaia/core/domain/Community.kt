package com.yuyakaido.gaia.core.domain

import android.net.Uri

data class Community(
    val id: String,
    val name: String,
    val icon: Uri
) {
    fun display(): String {
        return "r/$name"
    }
}