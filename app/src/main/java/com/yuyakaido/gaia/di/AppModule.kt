package com.yuyakaido.gaia.di

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yuyakaido.gaia.account.AccountApi
import com.yuyakaido.gaia.article.ArticleApi
import com.yuyakaido.gaia.auth.AuthApi
import com.yuyakaido.gaia.auth.AuthInterceptor
import com.yuyakaido.gaia.auth.BasicAuthInterceptor
import com.yuyakaido.gaia.auth.TokenAuthenticator
import com.yuyakaido.gaia.domain.Kind
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@ExperimentalSerializationApi
@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        ActivityModule::class
    ]
)
class AppModule {

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        val json = Json {
            ignoreUnknownKeys = true
            classDiscriminator = Kind.classDiscriminator
        }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
    }

    @OkHttpClientForPublic
    @Singleton
    @Provides
    fun provideOkHttpClientForPublic(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(StethoInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(BasicAuthInterceptor())
            .build()
    }

    @OkHttpClientForPrivate
    @Singleton
    @Provides
    fun provideOkHttpClientForPrivate(
        application: Application,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authApi: AuthApi
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(
                TokenAuthenticator(
                    application = application,
                    authApi = authApi
                )
            )
            .addInterceptor(StethoInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AuthInterceptor(application))
            .build()
    }

    @RetrofitForPublic
    @Singleton
    @Provides
    fun provideRetrofitForPublic(
        @OkHttpClientForPublic okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://www.reddit.com/api/v1/")
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @RetrofitForPrivate
    @Singleton
    @Provides
    fun provideRetrofitForPrivate(
        @OkHttpClientForPrivate okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://oauth.reddit.com/")
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthApi(
        @RetrofitForPublic retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAccountApi(
        @RetrofitForPrivate retrofit: Retrofit
    ): AccountApi {
        return retrofit.create(AccountApi::class.java)
    }

    @Singleton
    @Provides
    fun provideArticleApi(
        @RetrofitForPrivate retrofit: Retrofit
    ): ArticleApi {
        return retrofit.create(ArticleApi::class.java)
    }

}