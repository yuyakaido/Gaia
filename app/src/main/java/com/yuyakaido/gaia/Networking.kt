package com.yuyakaido.gaia

import android.app.Application
import android.net.Uri
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yuyakaido.gaia.auth.AuthApi
import com.yuyakaido.gaia.auth.AuthInterceptor
import com.yuyakaido.gaia.auth.BasicAuthInterceptor
import com.yuyakaido.gaia.auth.TokenAuthenticator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@ExperimentalSerializationApi
object Networking {

    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val converter = json.asConverterFactory("application/json".toMediaType())
    private val httpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    fun createOAuthUri(): Uri {
        val scopes = listOf(
            "creddits",
            "modcontributors",
            "modmail",
            "modconfig",
            "subscribe",
            "structuredstyles",
            "vote",
            "wikiedit",
            "mysubreddits",
            "submit",
            "modlog",
            "modposts",
            "modflair",
            "save",
            "modothers",
            "adsconversions",
            "read",
            "privatemessages",
            "report",
            "identity",
            "livemanage",
            "account",
            "modtraffic",
            "wikiread",
            "edit",
            "modwiki",
            "modself",
            "history",
            "flair"
        )
        return Uri.Builder()
            .scheme("https")
            .encodedAuthority("www.reddit.com")
            .encodedPath("api/v1/authorize.compact")
            .appendQueryParameter("client_id", BuildConfig.REDDIT_CLIENT_ID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("state", System.nanoTime().toString())
            .appendQueryParameter("redirect_uri", "com.yuyakaido.gaia://authorization")
            .appendQueryParameter("duration", "permanent")
            .appendQueryParameter("scope", scopes.joinToString(" "))
            .build()
    }

    fun createAuthApi(): AuthApi {
        val okHttpClientForPublic = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofitForPublic = Retrofit.Builder()
            .client(okHttpClientForPublic)
            .baseUrl("https://www.reddit.com/api/v1/")
            .addConverterFactory(converter)
            .build()
        return retrofitForPublic.create(AuthApi::class.java)
    }

    fun createMeApi(application: Application): MeApi {
        val okHttpClientForPrivate = OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(application))
            .addInterceptor(AuthInterceptor(application))
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofitForPrivate = Retrofit.Builder()
            .client(okHttpClientForPrivate)
            .baseUrl("https://oauth.reddit.com/api/v1/")
            .addConverterFactory(converter)
            .build()
        return retrofitForPrivate.create(MeApi::class.java)
    }

}