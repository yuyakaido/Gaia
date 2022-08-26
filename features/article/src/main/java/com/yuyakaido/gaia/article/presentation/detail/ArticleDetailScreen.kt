package com.yuyakaido.gaia.article.presentation.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.yuyakaido.gaia.core.presentation.ArticleContent
import com.yuyakaido.gaia.core.presentation.ArticleItem

@Composable
fun ArticleDetailScreen(
    viewModel: ArticleDetailViewModel
) {
    when (val state = viewModel.state.collectAsState().value) {
        is ArticleDetailViewModel.State.Loading -> {
            CircularProgressIndicator()
        }
        is ArticleDetailViewModel.State.Ideal -> {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ArticleItem(
                        content = ArticleContent(
                            article = state.article,
                            isProcessing = false
                        ),
                        showDetail = true,
                        onClickArticle = {},
                        onClickAuthor = {},
                        onToggleVote = {}
                    )
                }
            }
        }
    }
}