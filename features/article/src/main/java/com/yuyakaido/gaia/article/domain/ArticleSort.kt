package com.yuyakaido.gaia.article.domain

import com.yuyakaido.gaia.article.R

enum class ArticleSort(
    val path: String,
    val menu: Int
) {
    Best(path = "best", menu = R.id.menu_sort_best),
    New(path = "new", menu = R.id.menu_sort_new);

    companion object {
        fun from(menu: Int): ArticleSort {
            return values().first { it.menu == menu }
        }
    }
}