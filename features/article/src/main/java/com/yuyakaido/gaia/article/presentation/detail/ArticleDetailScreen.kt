package com.yuyakaido.gaia.article.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            val article = state.article
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.background)
            ) {
                item {
                    ArticleItem(
                        content = ArticleContent(
                            article = article,
                            isProcessing = false
                        ),
                        showDetail = true,
                        onClickArticle = {},
                        onClickAuthor = {},
                        onToggleVote = {}
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                items(article.comments) { comment ->
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(text = comment.author.display())
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(text = comment.body)
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    Spacer(modifier = Modifier.size(1.dp))
                }
            }
        }
    }
}