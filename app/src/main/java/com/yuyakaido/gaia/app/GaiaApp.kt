package com.yuyakaido.gaia.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.yuyakaido.gaia.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.ExperimentalSerializationApi
import timber.log.Timber

@ExperimentalSerializationApi
@HiltAndroidApp
class GaiaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeTimber()
        initializeStetho()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initializeStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

}