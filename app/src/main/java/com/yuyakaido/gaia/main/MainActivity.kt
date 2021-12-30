package com.yuyakaido.gaia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.yuyakaido.gaia.account.AccountViewModel
import com.yuyakaido.gaia.app.LauncherActivity
import com.yuyakaido.gaia.article.ArticleDetailViewModel
import com.yuyakaido.gaia.article.ArticleListViewModel
import com.yuyakaido.gaia.auth.OAuth
import com.yuyakaido.gaia.auth.Session
import com.yuyakaido.gaia.core.ViewModelFactory
import com.yuyakaido.gaia.message.MessageListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    @Inject
    internal lateinit var mainViewModelFactory: ViewModelFactory<MainViewModel>

    @Inject
    internal lateinit var articleListViewModelFactory: ViewModelFactory<ArticleListViewModel>

    @Inject
    internal lateinit var articleDetailViewModelFactory: ViewModelFactory<ArticleDetailViewModel>

    @Inject
    internal lateinit var messageListViewModelFactory: ViewModelFactory<MessageListViewModel>

    @Inject
    internal lateinit var accountViewModelFactory: ViewModelFactory<AccountViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                application = application,
                addNewAccount = {
                    startActivity(Intent(Intent.ACTION_VIEW, OAuth.uri))
                    finish()
                },
                activateSession = {
                    Session.activate(
                        application = application,
                        session = it
                    )
                    startActivity(LauncherActivity.createIntent(this))
                    finish()
                },
                mainViewModelFactory = mainViewModelFactory,
                articleListViewModelFactory = articleListViewModelFactory,
                articleDetailViewModelFactory = articleDetailViewModelFactory,
                messageListViewModelFactory = messageListViewModelFactory,
                accountViewModelFactory = accountViewModelFactory
            )
        }
    }

}