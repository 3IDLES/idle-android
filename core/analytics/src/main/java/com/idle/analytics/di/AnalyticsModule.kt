package com.idle.analytics.di

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.idle.analytics.BuildConfig
import com.idle.analytics.businessmetric.AmplitudeAnalyticsHelper
import com.idle.analytics.businessmetric.AnalyticsHelper
import com.idle.analytics.businessmetric.DebugAnalyticsHelper
import com.idle.analytics.error.CrashlyticsErrorLoggingHelper
import com.idle.analytics.error.DebugErrorLoggingHelper
import com.idle.analytics.error.ErrorLoggingHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Provides
    @Singleton
    fun providesAmplitude(@ApplicationContext context: Context): Amplitude = Amplitude(
        Configuration(
            apiKey = BuildConfig.AMPLITUDE_API_KEY,
            context = context,
        )
    )

    @Provides
    @Singleton
    @DebugHelper
    fun provideDebugAnalyticsHelper(): AnalyticsHelper = DebugAnalyticsHelper()

    @Provides
    @Singleton
    @ReleaseHelper
    fun provideReleaseAnalyticsHelper(amplitude: Amplitude): AnalyticsHelper =
        AmplitudeAnalyticsHelper(amplitude)

    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        @DebugHelper debugHelper: AnalyticsHelper,
        @ReleaseHelper releaseHelper: AnalyticsHelper
    ): AnalyticsHelper {
        return if (BuildConfig.BUILD_TYPE == "RELEASE") releaseHelper
        else debugHelper
    }

    @Provides
    @Singleton
    @DebugErrorHelper
    fun provideDebugErrorLoggingHelper(): ErrorLoggingHelper = DebugErrorLoggingHelper()

    @Provides
    @Singleton
    @ReleaseErrorHelper
    fun provideReleaseErrorLoggingHelper(firebaseCrashlytics: FirebaseCrashlytics): ErrorLoggingHelper =
        CrashlyticsErrorLoggingHelper(firebaseCrashlytics)

    @Provides
    @Singleton
    fun provideErrorLoggingHelper(
        @DebugErrorHelper debugErrorHelper: ErrorLoggingHelper,
        @ReleaseErrorHelper releaseErrorHelper: ErrorLoggingHelper,
    ): ErrorLoggingHelper {
        return if (BuildConfig.BUILD_TYPE == "DEBUG") releaseErrorHelper
        else debugErrorHelper
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DebugHelper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReleaseHelper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DebugErrorHelper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReleaseErrorHelper
