package com.yuyakaido.gaia.app

sealed class Screen(val route: String) {
    object ArticleList : Screen("articles")
    object ArticleDetail : Screen("articles/{id}") {
        fun createRoute(id: String): String {
            return "articles/$id"
        }
    }
}