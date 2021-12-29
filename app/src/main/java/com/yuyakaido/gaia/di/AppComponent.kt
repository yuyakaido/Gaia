package com.yuyakaido.gaia.di

import com.yuyakaido.gaia.app.GaiaApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@AppScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<GaiaApp> {
    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}