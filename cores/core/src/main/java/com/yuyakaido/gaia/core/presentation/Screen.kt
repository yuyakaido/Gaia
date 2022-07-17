package com.yuyakaido.gaia.core.presentation

sealed class Screen(val route: String) {
    object ArticleList : Screen("articles")
    object ArticleDetail : Screen("articles/{id}") {
        fun createRoute(id: String): String {
            return "articles/$id"
        }
    }
    object MessageList : Screen("messages")
    object Account : Screen("account")
}