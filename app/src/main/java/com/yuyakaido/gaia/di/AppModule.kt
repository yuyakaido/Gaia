package com.yuyakaido.gaia.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yuyakaido.gaia.account.infra.AccountApi
import com.yuyakaido.gaia.app.AppIntentResolver
import com.yuyakaido.gaia.article.infra.ArticleApi
import com.yuyakaido.gaia.auth.*
import com.yuyakaido.gaia.core.domain.Kind
import com.yuyakaido.gaia.core.infra.*
import com.yuyakaido.gaia.core.presentation.AppIntentResolverType
import com.yuyakaido.gaia.message.infra.MessageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAppIntentResolverType(): AppIntentResolverType {
        return AppIntentResolver()
    }

    @Singleton
    @Provides
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            classDiscriminator = Kind.classDiscriminator
            serializersModule = serializersModuleOf(BigDecimalSerializer)
        }
    }

    @Singleton
    @Provides
    fun provideConverterFactory(
        json: Json
    ): Converter.Factory {
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
        httpLoggingInterceptor: HttpLoggingInterceptor,
        sessionRepository: SessionRepository,
        authRepository: AuthRepository
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(
                TokenAuthenticator(
                    sessionRepository = sessionRepository,
                    authRepository = authRepository
                )
            )
            .addInterceptor(StethoInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AuthInterceptor(sessionRepository))
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

    @Singleton
    @Provides
    fun provideMessageApi(
        @RetrofitForPrivate retrofit: Retrofit
    ): MessageApi {
        return retrofit.create(MessageApi::class.java)
    }

}