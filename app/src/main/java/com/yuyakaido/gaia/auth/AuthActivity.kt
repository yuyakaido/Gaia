package com.yuyakaido.gaia.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.gaia.account.AccountApi
import com.yuyakaido.gaia.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.*
import javax.inject.Inject

@ExperimentalSerializationApi
@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    internal lateinit var authApi: AuthApi

    @Inject
    internal lateinit var accountApi: AccountApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let { uri ->
            lifecycleScope.launch {
                val code = uri.getQueryParameter("code") ?: ""

                val token = authApi.getAccessToken(code = code).toToken()
                val id = UUID.randomUUID().toString()
                var session = Session(
                    id = id,
                    name = "",
                    token = token
                )
                Session.put(
                    application = application,
                    session = session
                )

                val account = accountApi.getMe().toEntity()
                session = session.copy(name = account.name)
                Session.put(
                    application = application,
                    session = session
                )

                startActivity(MainActivity.createIntent(this@AuthActivity))
                finish()
            }
        }
    }

}