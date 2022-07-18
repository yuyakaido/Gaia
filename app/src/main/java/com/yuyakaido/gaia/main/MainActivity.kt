package com.yuyakaido.gaia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.yuyakaido.gaia.R
import com.yuyakaido.gaia.auth.domain.OAuth
import com.yuyakaido.gaia.auth.presentation.SessionList
import com.yuyakaido.gaia.databinding.ActivityMainBinding
import com.yuyakaido.gaia.launcher.LauncherActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val sessionListView by lazy { ComposeView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupTopAppBar()
        setupNavigationView()
        setupBottomNavigationView()
        observeState()
    }

    private fun requireNavController(): NavController {
        val navHomeFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
        return requireNotNull(navHomeFragment?.findNavController())
    }

    // https://developer.android.com/guide/navigation/navigation-ui#top_app_bar
    private fun setupTopAppBar() {
        setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.fragment_article_list,
                R.id.fragment_message_list,
                R.id.fragment_account
            ),
            drawerLayout = binding.drawerLayout
        )
        binding.toolbar.setupWithNavController(requireNavController(), appBarConfiguration)
    }

    // https://developer.android.com/guide/navigation/navigation-ui#add_a_navigation_drawer
    private fun setupNavigationView() {
        binding.navigationView.addView(sessionListView)
    }

    // https://developer.android.com/guide/navigation/navigation-ui#bottom_navigation
    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setupWithNavController(requireNavController())
    }

    private fun observeState() {
        viewModel.state
            .onEach { state ->
                sessionListView.setContent {
                    SessionList(
                        sessions = state.sessions,
                        addNewSession = { viewModel.onAddNewSession() },
                        activateSession = { viewModel.onActivateSession(it) }
                    )
                }
                state.events.firstOrNull()?.let { event ->
                    when (event) {
                        MainViewModel.Event.NavigateToAuth -> {
                            startActivity(Intent(Intent.ACTION_VIEW, OAuth.uri))
                        }
                        MainViewModel.Event.Relaunch -> {
                            startActivity(LauncherActivity.createIntent(this@MainActivity))
                        }
                    }
                    finish()
                    viewModel.onConsumeEvent(event)
                }
            }
            .launchIn(lifecycleScope)
    }

}