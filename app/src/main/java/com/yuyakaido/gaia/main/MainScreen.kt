package com.yuyakaido.gaia.main

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yuyakaido.gaia.R
import com.yuyakaido.gaia.account.AccountScreen
import com.yuyakaido.gaia.account.AccountViewModel
import com.yuyakaido.gaia.app.Screen
import com.yuyakaido.gaia.article.ArticleDetailScreen
import com.yuyakaido.gaia.article.ArticleDetailViewModel
import com.yuyakaido.gaia.article.ArticleListScreen
import com.yuyakaido.gaia.article.ArticleListViewModel
import com.yuyakaido.gaia.core.ViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun MainScreen(
    application: Application,
    articleListViewModelFactory: ViewModelFactory<ArticleListViewModel>,
    articleDetailViewModelFactory: ViewModelFactory<ArticleDetailViewModel>,
    accountViewModelFactory: ViewModelFactory<AccountViewModel>
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val openDrawer = {
        coroutineScope.launch {
            scaffoldState.drawerState.open()
        }
    }
    val closeDrawer = {
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MainTopBar(application = application) { openDrawer.invoke() } },
        bottomBar = { MainBottomBar(navController = navController) },
        drawerContent = { MainDrawer { closeDrawer.invoke() } }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.ArticleList.route
        ) {
            composable(route = Screen.ArticleList.route) {
                ArticleListScreen(
                    navController = navController,
                    viewModel = viewModel(
                        modelClass = ArticleListViewModel::class.java,
                        factory = articleListViewModelFactory
                    )
                )
            }
            composable(route = Screen.ArticleDetail.route) {
                val arguments = requireNotNull(it.arguments)
                val id = requireNotNull(arguments.getString("id"))
                val viewModel = viewModel(
                    modelClass = ArticleDetailViewModel::class.java,
                    factory = articleDetailViewModelFactory
                )
                viewModel.onCreate(id = id)
                ArticleDetailScreen(viewModel = viewModel)
            }
            composable(route = Screen.Account.route) {
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
fun MainTopBar(
    application: Application,
    onOpenDrawer: () -> Unit
) {
    TopAppBar(
        title = { Text(text = application.getString(R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = { onOpenDrawer.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun MainBottomBar(
    navController: NavController
) {
    BottomNavigation {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route
        MainNavigationItem.values().forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name
                    )
                },
                label = {
                    Text(text = item.name)
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (item.route != currentRoute) {
                        navController.navigate(item.route) {
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

@Composable
fun MainDrawer(
    closeDrawer: () -> Unit
) {
    Column {
        MainDrawerItem.values().forEach {
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxSize()
                    .clickable {
                        closeDrawer.invoke()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.name,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}