package com.yuyakaido.gaia.article.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yuyakaido.gaia.core.R
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.domain.Author
import com.yuyakaido.gaia.core.presentation.ArticleContent
import com.yuyakaido.gaia.core.presentation.ArticleItem

@Composable
fun ArticleListScreen(
    contents: List<ArticleContent>,
    isRefreshing: Boolean,
    isError: Boolean,
    onRefresh: () -> Unit,
    onPaginate: () -> Unit,
    onClickArticle: (article: Article) -> Unit,
    onClickAuthor: (author: Author) -> Unit,
    onToggleVote: (article: Article) -> Unit
) {
    if (isError) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.something_went_wrong))
                TextButton(onClick = { onRefresh.invoke() }) {
                    Text(text = stringResource(id = R.string.retry))
                }
            }
        }
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = { onRefresh.invoke() },
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contents) {
                    ArticleItem(
                        content = it,
                        onClickArticle = onClickArticle,
                        onClickAuthor = onClickAuthor,
                        onToggleVote = onToggleVote
                    )
                }
                if (contents.isNotEmpty()) {
                    item {
                        LoadingIndicator { onPaginate.invoke() }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator(
    onPaginate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp
        )
    }
    LaunchedEffect(Unit) {
        onPaginate.invoke()
    }
}