package com.yuyakaido.gaia.article.presentation.list

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yuyakaido.gaia.core.domain.Article

@Composable
fun ArticleListScreen(
    articles: List<Article>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onPaginate: () -> Unit,
    onClick: (article: Article) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh.invoke() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 16.dp
            )
        ) {
            items(articles) {
                ArticleItem(
                    article = it,
                    onClick = onClick
                )
            }
            if (articles.isNotEmpty()) {
                item {
                    LoadingIndicator { onPaginate.invoke() }
                }
            }
        }
    }
}

@Composable
fun ArticleItem(
    article: Article,
    onClick: (article: Article) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onClick.invoke(article) }
    ) {
        ThumbnailImage(uri = article.thumbnail)
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = article.title,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ThumbnailImage(uri: Uri) {
    val width = 100.dp
    val height = 80.dp
    if (uri == Uri.EMPTY) {
        Canvas(
            modifier = Modifier.size(
                width = width,
                height = height
            ),
            onDraw = {
                drawRect(Color.LightGray)
            }
        )
    } else {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.size(
                width = width,
                height = height
            ),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
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