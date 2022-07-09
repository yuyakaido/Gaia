package com.yuyakaido.gaia.main

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yuyakaido.gaia.R
import com.yuyakaido.gaia.account.AccountScreen
import com.yuyakaido.gaia.account.AccountViewModel
import com.yuyakaido.gaia.article.ArticleDetailScreen
import com.yuyakaido.gaia.article.ArticleDetailViewModel
import com.yuyakaido.gaia.article.ArticleListScreen
import com.yuyakaido.gaia.core.domain.Session
import com.yuyakaido.gaia.core.presentation.Screen
import com.yuyakaido.gaia.message.MessageListScreen
import com.yuyakaido.gaia.message.MessageListViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun MainScreen(
    application: Application,
    addNewAccount: () -> Unit,
    activateSession: (session: Session) -> Unit
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val openDrawer: () -> Unit = {
        coroutineScope.launch {
            scaffoldState.drawerState.open()
        }
    }
    val closeDrawer: () -> Unit = {
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
    }

    val mainViewModel = hiltViewModel<MainViewModel>()
    val sessions = mainViewModel.sessions.value

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MainTopBar(application = application) { openDrawer.invoke() } },
        bottomBar = { MainBottomBar(navController = navController) },
        drawerContent = {
            MainDrawer(
                sessions = sessions,
                addNewAccount = addNewAccount,
                activateSession = activateSession,
                closeDrawer = closeDrawer
            )
        }
    ) {
        Surface(
            modifier = Modifier.padding(
                bottom = it.calculateBottomPadding()
            )
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.ArticleList.route
            ) {
                composable(route = Screen.ArticleList.route) {
                    ArticleListScreen(
                        navController = navController,
                        viewModel = hiltViewModel()
                    )
                }
                composable(
                    route = Screen.ArticleDetail.route,
                    arguments = listOf(
                        navArgument("id") { type = NavType.StringType }
                    )
                ) {
                    val viewModel = hiltViewModel<ArticleDetailViewModel>()
                    ArticleDetailScreen(viewModel = viewModel)
                }
                composable(route = Screen.MessageList.route) {
                    val viewModel = hiltViewModel<MessageListViewModel>()
                    MessageListScreen(
                        viewModel = viewModel,
                        onMessageClicked = { message ->
                            Toast.makeText(application, message.body, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                composable(route = Screen.Account.route) {
                    val viewModel = hiltViewModel<AccountViewModel>()
                    AccountScreen(viewModel = viewModel)
                }
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
    sessions: List<Session>,
    addNewAccount: () -> Unit,
    activateSession: (session: Session) -> Unit,
    closeDrawer: () -> Unit,
) {
    val height = 50.dp
    Column {
        LazyColumn {
            items(sessions) {
                Row(
                    modifier = Modifier
                        .height(height = height)
                        .fillMaxSize()
                        .clickable {
                            activateSession.invoke(it)
                            closeDrawer.invoke()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (it.isActive) {
                            "\uD83D\uDD35 ${it.name}"
                        } else {
                            it.name
                        },
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .height(height = height)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(onClick = { addNewAccount.invoke() }) {
                Text(text = "Add new account")
            }
        }
    }
}