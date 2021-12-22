package com.yuyakaido.gaia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MainActivity : ComponentActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val api = Networking.createArticleApi(application)
            val articles = api.getPopularArticles().toArticles()
            setContent {
                ArticleList(articles)
            }
        }
    }

}

@Composable
fun ArticleItem(article: Article) {
    Text(article.title)
}

@Composable
fun ArticleList(articles: List<Article>) {
    LazyColumn {
        items(articles) {
            ArticleItem(it)
        }
    }
}