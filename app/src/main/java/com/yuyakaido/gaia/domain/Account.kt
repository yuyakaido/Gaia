package com.yuyakaido.gaia.domain

import android.net.Uri

data class Account(
    val id: String,
    val name: String,
    val icon: Uri
)