package com.yuyakaido.gaia.di

import com.yuyakaido.gaia.launcher.LauncherActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@InstallIn(SingletonComponent::class)
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeLauncherActivity(): LauncherActivity

}