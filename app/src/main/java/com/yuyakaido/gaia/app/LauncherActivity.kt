package com.yuyakaido.gaia.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.gaia.auth.OAuth
import com.yuyakaido.gaia.main.MainActivity
import com.yuyakaido.gaia.session.SessionRepository
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.launch
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
    internal lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val session = sessionRepository.getActiveSession()
            if (session == null) {
                startActivity(Intent(Intent.ACTION_VIEW, OAuth.uri))
            } else {
                startActivity(MainActivity.createIntent(this@LauncherActivity))
            }
            finish()
        }
    }

}