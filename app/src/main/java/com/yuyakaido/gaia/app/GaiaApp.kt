package com.yuyakaido.gaia.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.yuyakaido.gaia.BuildConfig
import com.yuyakaido.gaia.di.AppComponent
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import dagger.hilt.EntryPoints
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.ExperimentalSerializationApi
import timber.log.Timber

@ExperimentalSerializationApi
@HiltAndroidApp
class GaiaApp : Application(), HasAndroidInjector {

    private val appComponent: AppComponent by lazy {
        EntryPoints.get(this, AppComponent::class.java)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return appComponent.androidInjector()
    }

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