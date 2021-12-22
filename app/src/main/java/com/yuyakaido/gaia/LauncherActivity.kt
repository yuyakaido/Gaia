package com.yuyakaido.gaia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yuyakaido.gaia.auth.Session
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val session = Session.get(application)
        if (session == null) {
            val uri = Networking.createOAuthUri()
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } else {
            startActivity(MainActivity.createIntent(this))
        }
        finish()
    }

}