package com.yuyakaido.gaia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yuyakaido.gaia.app.LauncherActivity
import com.yuyakaido.gaia.auth.OAuth
import com.yuyakaido.gaia.auth.Session
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                application = application,
                addNewAccount = {
                    startActivity(Intent(Intent.ACTION_VIEW, OAuth.uri))
                    finish()
                },
                activateSession = {
                    Session.activate(
                        application = application,
                        session = it
                    )
                    startActivity(LauncherActivity.createIntent(this))
                    finish()
                }
            )
        }
    }

}