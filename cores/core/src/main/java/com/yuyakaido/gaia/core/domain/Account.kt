package com.yuyakaido.gaia.core.domain

import android.net.Uri
import java.time.ZonedDateTime

data class Account(
    val id: String,
    val name: String,
    val icon: Uri,
    val createdAt: ZonedDateTime,
    val totalKarma: Int,
    val postKarma: Int,
    val commentKarma: Int,
    val awarderKarma: Int,
    val awardeeKarma: Int
)