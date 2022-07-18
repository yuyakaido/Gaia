package com.yuyakaido.gaia.account.domain

import android.net.Uri
import java.time.ZonedDateTime

data class Account(
    val id: String,
    val name: String,
    val icon: Uri,
    val totalKarma: Int,
    val createdAt: ZonedDateTime
)