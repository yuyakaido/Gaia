package com.yuyakaido.gaia.core.presentation

import android.content.Context
import android.content.Intent

interface AppIntentResolverType {
    fun getMainActivityIntent(content: Context): Intent
}