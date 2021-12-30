package com.yuyakaido.gaia.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.yuyakaido.gaia.app.Screen

enum class MainNavigationItem(
    val route: String,
    val icon: ImageVector
) {
    Home(
        route = Screen.ArticleList.route,
        icon = Icons.Default.Home
    ),
    Message(
        route = Screen.MessageList.route,
        icon = Icons.Default.Email
    ),
    Account(
        route = Screen.Account.route,
        icon = Icons.Default.AccountCircle
    )
}