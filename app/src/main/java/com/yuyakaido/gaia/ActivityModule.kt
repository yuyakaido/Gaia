package com.yuyakaido.gaia

import com.yuyakaido.gaia.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}