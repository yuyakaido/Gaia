package com.yuyakaido.gaia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import coil.compose.rememberImagePainter
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MainActivity : ComponentActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                context = applicationContext,
                viewModel = viewModel
            )
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

@Composable
fun ArticleItem(
    context: Context,
    article: Article
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                Toast
                    .makeText(context, article.title, Toast.LENGTH_SHORT)
                    .show()
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

@ExperimentalSerializationApi
@Composable
fun ArticleList(
    context: Context,
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
                context = context,
                article = it
            )
        }
    }
}

@ExperimentalSerializationApi
@Composable
fun StateView(state: MainViewModel.State) {
    Text(
        text = state::class.java.simpleName,
        fontSize = 32.sp
    )
}

@ExperimentalSerializationApi
@Composable
fun MainScreen(
    context: Context,
    viewModel: MainViewModel
) {
    viewModel.state.value.let {
        when (it) {
            is MainViewModel.State.Initial,
            is MainViewModel.State.Loading,
            is MainViewModel.State.Error -> {
                StateView(state = it)
            }
            is MainViewModel.State.Ideal -> {
                ArticleList(
                    context = context,
                    articles = it.articles
                )
            }
        }
    }
}
