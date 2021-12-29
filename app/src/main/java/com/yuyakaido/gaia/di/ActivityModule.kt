package com.yuyakaido.gaia.di

import com.yuyakaido.gaia.app.LauncherActivity
import com.yuyakaido.gaia.auth.AuthActivity
import com.yuyakaido.gaia.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}