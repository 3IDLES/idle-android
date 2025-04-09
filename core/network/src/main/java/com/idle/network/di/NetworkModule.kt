package com.idle.network.di

import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.idle.domain.model.notification.Notification
import com.idle.network.BuildConfig
import com.idle.network.api.AuthApi
import com.idle.network.api.ChatApi
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.WebSocketClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideJson(
        notificationSerializer: NotificationSerializer
    ): Json = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            contextual(Notification::class, notificationSerializer)
        }
    }

    @Singleton
    @Provides
    @AuthOkHttpClient
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authenticator: CareAuthenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    @WebSocketOkHttpClient
    fun provideWebSocketOkHttpClient(
        authenticator: CareAuthenticator,
    ): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(1, TimeUnit.MINUTES)
        .pingInterval(10, TimeUnit.SECONDS)
        .authenticator(authenticator)
        .apply {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(loggingInterceptor)
            }
        }.build()

    @Singleton
    @Provides
    fun providesWebSocketClient(
        @WebSocketOkHttpClient okHttpClient: OkHttpClient
    ): WebSocketClient = OkHttpWebSocketClient(okHttpClient)

    @Singleton
    @Provides
    fun providesAuthApi(
        json: Json,
        @AuthOkHttpClient okHttpClient: OkHttpClient,
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
        @AuthOkHttpClient okHttpClient: OkHttpClient,
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
        @AuthOkHttpClient okHttpClient: OkHttpClient,
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
        @AuthOkHttpClient okHttpClient: OkHttpClient,
    ): NotificationApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.CARE_BASE_URL)
        .build()
        .create(NotificationApi::class.java)

    @Singleton
    @Provides
    fun providesChatApi(
        json: Json,
        @AuthOkHttpClient okHttpClient: OkHttpClient,
    ): ChatApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.CARE_BASE_URL)
        .build()
        .create(ChatApi::class.java)

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        setConfigSettingsAsync(configSettings)
    }

    @Singleton
    @Provides
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebSocketOkHttpClient
