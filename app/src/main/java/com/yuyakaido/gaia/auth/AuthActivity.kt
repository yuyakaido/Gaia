package com.yuyakaido.gaia.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.gaia.MainActivity
import com.yuyakaido.gaia.Networking
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let { uri ->
            lifecycleScope.launch {
                val code = uri.getQueryParameter("code") ?: ""

                val api = Networking.createAuthApi()
                val response = api.getAccessToken(code = code)
                val session = response.toSession()
                Session.put(application, session)

                startActivity(MainActivity.createIntent(this@AuthActivity))
                finish()
            }
        }
    }

}