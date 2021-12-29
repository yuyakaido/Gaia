package com.yuyakaido.gaia

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun GaiaApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(
                navController = navController,
                viewModel = viewModel()
            )
        }
        composable(route = Screen.ArticleDetail.route) {
            val arguments = requireNotNull(it.arguments)
            val id = requireNotNull(arguments.getString("id"))
            val viewModel = viewModel(ArticleDetailViewModel::class.java)
            viewModel.onCreate(id = id)
            ArticleDetailScreen(viewModel = viewModel)
        }
    }
}