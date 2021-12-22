package com.yuyakaido.gaia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val session = Session.get(this)
        if (session == null) {
            val scopes = listOf(
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
            )
            val uri = Uri.Builder()
                .scheme("https")
                .encodedAuthority("www.reddit.com")
                .encodedPath("api/v1/authorize.compact")
                .appendQueryParameter("client_id", BuildConfig.REDDIT_CLIENT_ID)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("state", System.nanoTime().toString())
                .appendQueryParameter("redirect_uri", "com.yuyakaido.gaia://authorization")
                .appendQueryParameter("duration", "permanent")
                .appendQueryParameter("scope", scopes.joinToString(" "))
                .build()
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } else {
            startActivity(MainActivity.createIntent(this))
        }
        finish()
    }

}