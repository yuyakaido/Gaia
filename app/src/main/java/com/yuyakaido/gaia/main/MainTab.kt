package com.yuyakaido.gaia.main

import com.yuyakaido.gaia.R
import com.yuyakaido.gaia.app.Destination

enum class MainTab(
    val route: String,
    val icon: Int
) {
    Home(
        route = Destination.ArticleList.route,
        icon = R.drawable.ic_home
    ),
    Account(
        route = Destination.Account.route,
        icon = R.drawable.ic_account
    )
}