package com.yuyakaido.gaia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
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
        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        val navHomeFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
        navHomeFragment?.findNavController()?.let { navController ->
            binding.bottomNavigationView.setupWithNavController(navController)
        }
    }

}