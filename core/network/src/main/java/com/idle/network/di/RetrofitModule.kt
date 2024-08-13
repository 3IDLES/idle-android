package com.idle.network.di

import com.idle.network.BuildConfig
import com.idle.network.api.CareApi
import com.idle.network.authenticator.CareAuthenticator
import com.idle.network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        careAuthenticator: CareAuthenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(careAuthenticator)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    fun providesCareApi(
        okHttpClient: OkHttpClient,
    ): CareApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.CARE_BASE_URL)
        .build()
        .create(CareApi::class.java)
}