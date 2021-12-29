package com.yuyakaido.gaia.article

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun ArticleDetailScreen(
    viewModel: ArticleDetailViewModel
) {
    when (val state = viewModel.state.value) {
        is ArticleDetailViewModel.State.Initial -> {
            Text(text = state::class.java.simpleName)
        }
        is ArticleDetailViewModel.State.Ideal -> {
            Text(text = state.article.title)
        }
    }
}