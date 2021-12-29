package com.yuyakaido.gaia.article

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.yuyakaido.gaia.domain.Article
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun ArticleDetailScreen(
    viewModel: ArticleDetailViewModel
) {
    when (val state = viewModel.state.value) {
        is ArticleDetailViewModel.State.Initial -> {
            StateView(state = ArticleDetailViewModel.State.Initial::class.java.simpleName)
        }
        is ArticleDetailViewModel.State.Ideal -> {
            ArticleDetailView(article = state.article)
        }
    }
}

@Composable
fun ArticleDetailView(article: Article) {
    Text(
        text = article.title,
        fontSize = 32.sp,
    )
}