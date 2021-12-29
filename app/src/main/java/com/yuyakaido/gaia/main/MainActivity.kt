package com.yuyakaido.gaia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.yuyakaido.gaia.account.AccountViewModel
import com.yuyakaido.gaia.article.ArticleDetailViewModel
import com.yuyakaido.gaia.article.ArticleListViewModel
import com.yuyakaido.gaia.core.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class MainActivity : DaggerAppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    @Inject
    internal lateinit var articleListViewModelFactory: ViewModelFactory<ArticleListViewModel>

    @Inject
    internal lateinit var articleDetailViewModelFactory: ViewModelFactory<ArticleDetailViewModel>

    @Inject
    internal lateinit var accountViewModelFactory: ViewModelFactory<AccountViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                articleListViewModelFactory = articleListViewModelFactory,
                articleDetailViewModelFactory = articleDetailViewModelFactory,
                accountViewModelFactory = accountViewModelFactory
            )
        }
    }

}