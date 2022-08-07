package com.yuyakaido.gaia.article.presentation.list

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
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
import com.yuyakaido.gaia.core.domain.Author
import com.yuyakaido.gaia.core.domain.Community

@Composable
fun ArticleListScreen(
    contents: List<ArticleListViewModel.Content>,
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
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
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
fun ArticleItem(
    content: ArticleListViewModel.Content,
    onClickArticle: (article: Article) -> Unit,
    onClickAuthor: (author: Author) -> Unit,
    onToggleVote: (article: Article) -> Unit
) {
    val article = content.article
    val isProcessing = content.isProcessing
    Column(
        modifier = Modifier.clickable { onClickArticle.invoke(article) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (val community = article.community) {
                is Community.Summary -> {
                    Canvas(
                        modifier = Modifier.size(32.dp),
                        onDraw = { drawCircle(Color.LightGray) }
                    )
                }
                is Community.Detail -> {
                    AsyncImage(
                        model = community.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(
                    text = article.community.display(),
                    maxLines = 1,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = article.author.display(),
                    modifier = Modifier.clickable { onClickAuthor.invoke(article.author) },
                    maxLines = 1,
                    style = MaterialTheme.typography.caption
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Row {
            ThumbnailImage(uri = article.thumbnail)
            Spacer(modifier = Modifier.size(8.dp))
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
                    if (isProcessing) {
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(16.dp)
                                    .requiredWidthIn(),
                                strokeWidth = 2.dp
                            )
                        }
                    } else if (article.likes == true) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                    }
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