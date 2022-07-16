package com.yuyakaido.gaia.article

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ArticleDetailScreen(
    viewModel: ArticleDetailViewModel
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        is ArticleDetailViewModel.State.Loading -> {
            CircularProgressIndicator()
        }
        is ArticleDetailViewModel.State.Ideal -> {
            Text(text = s.article.title)
        }
    }
}