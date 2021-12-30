package com.yuyakaido.gaia.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yuyakaido.gaia.auth.AuthApi
import com.yuyakaido.gaia.auth.OAuth
import com.yuyakaido.gaia.auth.Session
import com.yuyakaido.gaia.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class LauncherActivity : DaggerAppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LauncherActivity::class.java)
        }
    }

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