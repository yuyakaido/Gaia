package com.yuyakaido.gaia.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.yuyakaido.gaia.article.ArticleDetailViewModel
import com.yuyakaido.gaia.article.ArticleListViewModel
import com.yuyakaido.gaia.core.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class HomeActivity : DaggerAppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    @Inject
    internal lateinit var articleListViewModelFactory: ViewModelFactory<ArticleListViewModel>

    @Inject
    internal lateinit var articleDetailViewModelFactory: ViewModelFactory<ArticleDetailViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(
                articleListViewModelFactory = articleListViewModelFactory,
                articleDetailViewModelFactory = articleDetailViewModelFactory
            )
        }
    }

}
