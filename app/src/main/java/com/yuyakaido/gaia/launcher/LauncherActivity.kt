package com.yuyakaido.gaia.launcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.gaia.auth.OAuth
import com.yuyakaido.gaia.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@AndroidEntryPoint
class LauncherActivity : DaggerAppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LauncherActivity::class.java)
        }
    }

    private val viewModel by viewModels<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                state.events.firstOrNull()?.let { event ->
                    when (event) {
                        LauncherViewModel.Event.NavigateToAuth -> {
                            startActivity(Intent(Intent.ACTION_VIEW, OAuth.uri))
                        }
                        LauncherViewModel.Event.NavigateToMain -> {
                            startActivity(MainActivity.createIntent(this@LauncherActivity))
                        }
                    }
                    finish()
                    viewModel.consume(event)
                }
            }
        }
    }

}