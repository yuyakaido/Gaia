package com.yuyakaido.gaia.core.presentation

import androidx.navigation.NavController

interface AppNavigatorType {
    fun navigateToAccount(navController: NavController, name: String)
}