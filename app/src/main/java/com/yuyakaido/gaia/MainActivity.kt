package com.yuyakaido.gaia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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
    internal lateinit var mainViewModelFactory: ViewModelFactory<MainViewModel>

    @Inject
    internal lateinit var articleDetailViewModelFactory: ViewModelFactory<ArticleDetailViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GaiaAppScreen(
                mainViewModelFactory = mainViewModelFactory,
                articleDetailViewModelFactory = articleDetailViewModelFactory
            )
        }
    }

}
