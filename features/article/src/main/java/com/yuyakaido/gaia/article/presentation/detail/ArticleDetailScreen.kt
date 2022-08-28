package com.yuyakaido.gaia.article.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
                            isProcessing = state.isProcessing
                        ),
                        showDetail = true,
                        onClickArticle = {},
                        onClickAuthor = {},
                        onToggleVote = { viewModel.onToggleVote() },
                        onClickShare = {}
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                val flattenedComments = article.flattenedComments()
                items(flattenedComments) { comment ->
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .height(IntrinsicSize.Min)
                        ) {
                            (0 until comment.depth).forEach { _ ->
                                Divider(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .fillMaxHeight()
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text(
                                    text = comment.author.display(),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(text = comment.body)
                            }
                        }
                    }
                }
            }
        }
    }
}