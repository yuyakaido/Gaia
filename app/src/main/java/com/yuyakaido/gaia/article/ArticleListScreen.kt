package com.yuyakaido.gaia.article

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yuyakaido.gaia.app.Screen
import com.yuyakaido.gaia.domain.Article
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun ArticleListScreen(
    navController: NavController,
    viewModel: ArticleListViewModel
) {
    val items = viewModel.items.collectAsLazyPagingItems()
    ArticleList(
        navController = navController,
        pagingItems = items
    )
}

@ExperimentalSerializationApi
@Composable
fun ArticleList(
    navController: NavController,
    pagingItems: LazyPagingItems<Article>
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = pagingItems.loadState.prepend is LoadState.Loading
                    || pagingItems.loadState.append is LoadState.Loading
                    || pagingItems.loadState.refresh is LoadState.Loading
        ),
        onRefresh = { pagingItems.refresh() }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 16.dp
            )
        ) {
            items(pagingItems) {
                it?.let {
                    ArticleItem(
                        navController = navController,
                        article = it
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleItem(
    navController: NavController,
    article: Article
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(
                    route = Screen.ArticleDetail.createRoute(article.id)
                )
            }
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
        Image(
            painter = rememberImagePainter(
                data = uri.toString()
            ),
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