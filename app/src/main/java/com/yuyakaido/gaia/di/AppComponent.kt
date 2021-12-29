package com.yuyakaido.gaia.di

import com.yuyakaido.gaia.app.GaiaApp
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@InstallIn(SingletonComponent::class)
@EntryPoint
interface AppComponent : AndroidInjector<GaiaApp> {
    fun androidInjector(): DispatchingAndroidInjector<Any>
}