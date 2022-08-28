package com.yuyakaido.gaia.core.misc

import android.app.Activity
import android.content.Intent
import com.yuyakaido.gaia.core.domain.Article

object Sharing {

    fun share(article: Article, activity: Activity) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, article.url.toString())
        intent.type = "text/plain"
        activity.startActivity(Intent.createChooser(intent, article.title))
    }

}