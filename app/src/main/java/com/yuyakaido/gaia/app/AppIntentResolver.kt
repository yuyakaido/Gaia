package com.yuyakaido.gaia.app

import android.content.Context
import android.content.Intent
import com.yuyakaido.gaia.core.presentation.AppIntentResolverType
import com.yuyakaido.gaia.main.MainActivity

class AppIntentResolver : AppIntentResolverType {

    override fun getMainActivityIntent(content: Context): Intent {
        return MainActivity.createIntent(content)
    }

}