package com.yuyakaido.gaia

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yuyakaido.gaia.auth.AuthApi
import com.yuyakaido.gaia.auth.AuthInterceptor
import com.yuyakaido.gaia.auth.BasicAuthInterceptor
import com.yuyakaido.gaia.auth.TokenAuthenticator
import dagger.Module
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

@ExperimentalSerializationApi
@Module
class AppModule(
    private val application: Application
) {

    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        val json = Json {
            ignoreUnknownKeys = true
            classDiscriminator = Kind.classDiscriminator
        }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
    }

    @OkHttpClientForPublic
    @Provides
    fun provideOkHttpClientForPublic(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @OkHttpClientForPrivate
    @Provides
    fun provideOkHttpClientForPrivate(
        application: Application,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(application))
            .addInterceptor(AuthInterceptor(application))
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @RetrofitForPublic
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

    @Provides
    fun provideAuthApi(
        @RetrofitForPublic retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun provideArticleApi(
        @RetrofitForPrivate retrofit: Retrofit
    ): ArticleApi {
        return retrofit.create(ArticleApi::class.java)
    }

}