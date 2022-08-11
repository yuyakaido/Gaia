package com.yuyakaido.gaia.core.extension

import android.net.Uri
import android.webkit.URLUtil

fun String?.toUriWithoutQuery(): Uri {
    return if (URLUtil.isNetworkUrl(this)) {
        Uri.parse(this)
            .buildUpon()
            .clearQuery()
            .build()
    } else {
        Uri.EMPTY
    }
}