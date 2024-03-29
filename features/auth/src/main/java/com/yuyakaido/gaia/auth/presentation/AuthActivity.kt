package com.yuyakaido.gaia.auth.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yuyakaido.gaia.core.presentation.AppIntentResolverType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel>()

    @Inject
    internal lateinit var appIntentResolver: AppIntentResolverType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeState()
        authorize()
    }

    private fun observeState() {
        viewModel.state.observe(this) { state ->
            state.events.firstOrNull()?.let { event ->
                when (event) {
                    is AuthViewModel.Event.NavigateToMain -> {
                        navigateToMain()
                    }
                }
                viewModel.consume(event)
            }
        }
    }

    private fun authorize() {
        intent.data?.let { uri ->
            val code = uri.getQueryParameter("code") ?: ""
            viewModel.onCodeReceived(code)
        }
    }

    private fun navigateToMain() {
        startActivity(appIntentResolver.getMainActivityIntent(this))
        finish()
    }

}