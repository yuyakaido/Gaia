package com.yuyakaido.gaia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.gaia.app.LauncherActivity
import com.yuyakaido.gaia.auth.OAuth
import com.yuyakaido.gaia.session.SessionRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    internal lateinit var sessionRepository: SessionRepository

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
                    lifecycleScope.launch {
                        sessionRepository.activateSession(it)
                        startActivity(LauncherActivity.createIntent(this@MainActivity))
                        finish()
                    }
                }
            )
        }
    }

}