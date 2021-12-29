package com.yuyakaido.gaia.main

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yuyakaido.gaia.account.AccountScreen
import com.yuyakaido.gaia.account.AccountViewModel
import com.yuyakaido.gaia.app.Destination
import com.yuyakaido.gaia.article.ArticleDetailScreen
import com.yuyakaido.gaia.article.ArticleDetailViewModel
import com.yuyakaido.gaia.article.ArticleListScreen
import com.yuyakaido.gaia.article.ArticleListViewModel
import com.yuyakaido.gaia.core.ViewModelFactory
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun MainScreen(
    articleListViewModelFactory: ViewModelFactory<ArticleListViewModel>,
    articleDetailViewModelFactory: ViewModelFactory<ArticleDetailViewModel>,
    accountViewModelFactory: ViewModelFactory<AccountViewModel>
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { MainNavigation(navController = navController) }
    ) {
        NavHost(
            navController = navController,
            startDestination = Destination.ArticleList.route
        ) {
            composable(route = Destination.ArticleList.route) {
                ArticleListScreen(
                    navController = navController,
                    viewModel = viewModel(
                        modelClass = ArticleListViewModel::class.java,
                        factory = articleListViewModelFactory
                    )
                )
            }
            composable(route = Destination.ArticleDetail.route) {
                val arguments = requireNotNull(it.arguments)
                val id = requireNotNull(arguments.getString("id"))
                val viewModel = viewModel(
                    modelClass = ArticleDetailViewModel::class.java,
                    factory = articleDetailViewModelFactory
                )
                viewModel.onCreate(id = id)
                ArticleDetailScreen(viewModel = viewModel)
            }
            composable(route = Destination.Account.route) {
                val viewModel = viewModel(
                    modelClass = AccountViewModel::class.java,
                    factory = accountViewModelFactory
                )
                AccountScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainNavigation(
    navController: NavController
) {
    BottomNavigation(
        backgroundColor = Color.DarkGray,
        contentColor = Color.White
    ) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route
        MainTab.values().forEach { tab ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = tab.icon),
                        contentDescription = tab.name
                    )
                },
                label = {
                    Text(
                        text = tab.name,
                        fontSize = 10.sp
                    )
                },
                selected = currentRoute == tab.route,
                onClick = {
                    if (tab.route != currentRoute) {
                        navController.navigate(tab.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                }
                            }
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}