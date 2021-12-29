package com.yuyakaido.gaia.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuyakaido.gaia.article.ArticleDetailScreen
import com.yuyakaido.gaia.article.ArticleDetailViewModel
import com.yuyakaido.gaia.article.ArticleListScreen
import com.yuyakaido.gaia.article.ArticleListViewModel
import com.yuyakaido.gaia.core.ViewModelFactory
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun HomeScreen(
    articleListViewModelFactory: ViewModelFactory<ArticleListViewModel>,
    articleDetailViewModelFactory: ViewModelFactory<ArticleDetailViewModel>
) {
    val navController = rememberNavController()
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
    }
}