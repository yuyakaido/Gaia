package com.yuyakaido.gaia.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yuyakaido.gaia.auth.AuthApi
import com.yuyakaido.gaia.auth.OAuth
import com.yuyakaido.gaia.auth.Session
import com.yuyakaido.gaia.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    @Inject
    internal lateinit var authApi: AuthApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val session = Session.get(application)
        if (session == null) {
            startActivity(Intent(Intent.ACTION_VIEW, OAuth.uri))
        } else {
            startActivity(MainActivity.createIntent(this))
        }
        finish()
    }

}