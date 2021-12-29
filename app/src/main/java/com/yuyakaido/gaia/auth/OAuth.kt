package com.yuyakaido.gaia.auth

import android.net.Uri
import com.yuyakaido.gaia.BuildConfig

object OAuth {

    val uri: Uri = Uri.Builder()
        .scheme("https")
        .encodedAuthority("www.reddit.com")
        .encodedPath("api/v1/authorize.compact")
        .appendQueryParameter("client_id", BuildConfig.REDDIT_CLIENT_ID)
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("state", System.nanoTime().toString())
        .appendQueryParameter("redirect_uri", "com.yuyakaido.gaia://authorization")
        .appendQueryParameter("duration", "permanent")
        .appendQueryParameter(
            "scope",
            listOf(
                "creddits",
                "modcontributors",
                "modmail",
                "modconfig",
                "subscribe",
                "structuredstyles",
                "vote",
                "wikiedit",
                "mysubreddits",
                "submit",
                "modlog",
                "modposts",
                "modflair",
                "save",
                "modothers",
                "adsconversions",
                "read",
                "privatemessages",
                "report",
                "identity",
                "livemanage",
                "account",
                "modtraffic",
                "wikiread",
                "edit",
                "modwiki",
                "modself",
                "history",
                "flair"
            ).joinToString(" "))
        .build()

}