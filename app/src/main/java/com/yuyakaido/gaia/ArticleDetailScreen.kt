package com.yuyakaido.gaia

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ArticleDetailScreen(id: String) {
    ArticleDetailView(id = id)
}

@Composable
fun ArticleDetailView(id: String) {
    Text(
        text = id,
        fontSize = 32.sp,
    )
}