package com.yuyakaido.gaia.article.presentation.list

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yuyakaido.gaia.core.R
import com.yuyakaido.gaia.core.domain.Article

@Composable
fun ArticleListScreen(
    articles: List<Article>,
    isRefreshing: Boolean,
    isError: Boolean,
    onRefresh: () -> Unit,
    onPaginate: () -> Unit,
    onClickArticle: (article: Article) -> Unit,
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
                        onClickArticle = onClickArticle,
                        onToggleVote = onToggleVote
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
}

@Composable
fun ArticleItem(
    article: Article,
    onClickArticle: (article: Article) -> Unit,
    onToggleVote: (article: Article) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { onClickArticle.invoke(article) }
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
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.weight(1f)) {
                TextButton(
                    onClick = { onToggleVote.invoke(article) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = if (article.likes == true) {
                            Icons.Outlined.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = article.reactions.toString())
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                TextButton(
                    onClick = {},
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = article.numComments.toString())
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                TextButton(
                    onClick = {},
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = stringResource(id = R.string.share))
                }
            }
        }
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