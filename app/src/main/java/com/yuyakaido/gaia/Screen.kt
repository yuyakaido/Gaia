package com.yuyakaido.gaia

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object ArticleDetail : Screen("article/{id}") {
        fun createRoute(id: String): String {
            return "article/$id"
        }
    }
}