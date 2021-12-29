package com.yuyakaido.gaia

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    viewModel.state.value.let {
        when (it) {
            is MainViewModel.State.Initial,
            is MainViewModel.State.Loading,
            is MainViewModel.State.Error -> {
                StateView(state = it::class.java.simpleName)
            }
            is MainViewModel.State.Ideal -> {
                ArticleList(
                    navController = navController,
                    articles = it.articles
                )
            }
        }
    }
}

@ExperimentalSerializationApi
@Composable
fun ArticleList(
    navController: NavController,
    articles: List<Article>
) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 16.dp
        )
    ) {
        items(articles) {
            ArticleItem(
                navController = navController,
                article = it
            )
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

@ExperimentalSerializationApi
@Composable
fun StateView(state: String) {
    Text(
        text = state,
        fontSize = 32.sp
    )
}
