package com.yuyakaido.gaia.core.domain

import android.net.Uri

data class Article(
    val id: ID,
    val title: String,
    val thumbnail: Uri
) {
    data class ID(val value: String) {
        fun forPagination(): String {
            return "${Kind.article}_$value"
        }
    }
}