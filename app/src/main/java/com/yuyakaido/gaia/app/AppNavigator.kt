package com.yuyakaido.gaia.app

import androidx.navigation.NavController
import com.yuyakaido.gaia.NavigationMainDirections
import com.yuyakaido.gaia.core.presentation.AppNavigatorType

class AppNavigator : AppNavigatorType {

    override fun navigateToAccount(navController: NavController, name: String) {
        navController.navigate(NavigationMainDirections.actionAccount(name))
    }

}