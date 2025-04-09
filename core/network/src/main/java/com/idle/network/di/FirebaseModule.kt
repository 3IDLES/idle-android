package com.idle.network.di

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.idle.network.BuildConfig
import com.idle.network.source.error.CrashlyticsErrorLoggingHelper
import com.idle.network.source.error.DebugErrorLoggingHelper
import com.idle.network.source.error.ErrorLoggingHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
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

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()


    @Provides
    @Singleton
    @Debug
    fun provideDebugErrorLoggingHelper(): ErrorLoggingHelper =
        DebugErrorLoggingHelper()

    @Provides
    @Singleton
    @Release
    fun provideReleaseErrorLoggingHelper(firebaseCrashlytics: FirebaseCrashlytics): ErrorLoggingHelper =
        CrashlyticsErrorLoggingHelper(firebaseCrashlytics)

    @Provides
    @Singleton
    fun provideErrorLoggingHelper(
        @Debug debugErrorHelper: ErrorLoggingHelper,
        @Release releaseErrorHelper: ErrorLoggingHelper,
    ): ErrorLoggingHelper {
        return if (BuildConfig.DEBUG) debugErrorHelper
        else releaseErrorHelper
    }
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Debug

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Release
