package com.yuyakaido.gaia.auth

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.gaia.app.HomeActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class AuthActivity : DaggerAppCompatActivity() {

    @Inject
    internal lateinit var api: AuthApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let { uri ->
            lifecycleScope.launch {
                val code = uri.getQueryParameter("code") ?: ""

                val response = api.getAccessToken(code = code)
                val session = response.toSession()
                Session.put(application, session)

                startActivity(HomeActivity.createIntent(this@AuthActivity))
                finish()
            }
        }
    }

}