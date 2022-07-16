package com.yuyakaido.gaia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.yuyakaido.gaia.R
import com.yuyakaido.gaia.core.domain.SessionRepository
import com.yuyakaido.gaia.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @Inject
    internal lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupTopAppBar()
        setupNavigationView()
        setupBottomNavigationView()
    }

    private fun requireNavController(): NavController {
        val navHomeFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
        return requireNotNull(navHomeFragment?.findNavController())
    }

    // https://developer.android.com/guide/navigation/navigation-ui#top_app_bar
    private fun setupTopAppBar() {
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
        binding.navigationView.setupWithNavController(requireNavController())
    }

    // https://developer.android.com/guide/navigation/navigation-ui#bottom_navigation
    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setupWithNavController(requireNavController())
    }

}