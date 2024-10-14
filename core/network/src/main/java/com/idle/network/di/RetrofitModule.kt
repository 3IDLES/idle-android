package com.idle.network.di

import com.idle.network.BuildConfig
import com.idle.network.api.AuthApi
import com.idle.network.api.JobPostingApi
import com.idle.network.api.NotificationApi
import com.idle.network.api.UserApi
import com.idle.network.authenticator.CareAuthenticator
import com.idle.network.interceptor.AuthInterceptor
import com.idle.network.serializer.NotificationSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideJson(
        notificationSerializer: NotificationSerializer
    ): Json = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            contextual(NotificationSerializer())
        }
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
    fun providesAuthApi(
        json: Json,
        okHttpClient: OkHttpClient,
    ): AuthApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.CARE_BASE_URL)
        .build()
        .create(AuthApi::class.java)

    @Singleton
    @Provides
    fun providesJobPostingApi(
        json: Json,
        okHttpClient: OkHttpClient,
    ): JobPostingApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.CARE_BASE_URL)
        .build()
        .create(JobPostingApi::class.java)

    @Singleton
    @Provides
    fun providesUserApi(
        json: Json,
        okHttpClient: OkHttpClient,
    ): UserApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.CARE_BASE_URL)
        .build()
        .create(UserApi::class.java)

    @Singleton
    @Provides
    fun providesNotificationApi(
        json: Json,
        okHttpClient: OkHttpClient,
    ): NotificationApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.CARE_BASE_URL)
        .build()
        .create(NotificationApi::class.java)
}