package com.yuyakaido.gaia.app

sealed class Destination(val route: String) {
    object ArticleList : Destination("articles")
    object ArticleDetail : Destination("articles/{id}") {
        fun createRoute(id: String): String {
            return "articles/$id"
        }
    }
    object Account : Destination("account")
}