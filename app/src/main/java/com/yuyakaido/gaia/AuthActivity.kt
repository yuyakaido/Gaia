package com.yuyakaido.gaia

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let { uri ->
            Log.d("Gaia", "uri = $uri")
        }
    }

}