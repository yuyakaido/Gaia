package com.yuyakaido.gaia.core.domain

import android.net.Uri

data class Article(
    val id: String,
    val title: String,
    val thumbnail: Uri
)