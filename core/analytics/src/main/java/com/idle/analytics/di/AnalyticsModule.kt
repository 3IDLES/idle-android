package com.idle.analytics.di

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.idle.analytics.BuildConfig
import com.idle.analytics.helper.AmplitudeAnalyticsHelper
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.analytics.helper.DebugAnalyticsHelper
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
    fun providesAmplitude(@ApplicationContext context: Context): Amplitude = Amplitude(
        Configuration(
            apiKey = BuildConfig.AMPLITUDE_API_KEY,
            context = context,
        )
    )

    @Provides
    @Singleton
    @DebugLogger
    fun provideDebugAnalyticsHelper(): AnalyticsHelper = DebugAnalyticsHelper()

    @Provides
    @Singleton
    @ReleaseLogger
    fun provideReleaseAnalyticsHelper(
        amplitude: Amplitude
    ): AnalyticsHelper = AmplitudeAnalyticsHelper(amplitude)

    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        @DebugLogger debugHelper: AnalyticsHelper,
        @ReleaseLogger releaseHelper: AnalyticsHelper
    ): AnalyticsHelper {
        return if (BuildConfig.BUILD_TYPE == "RELEASE") {
            releaseHelper
        } else {
            debugHelper
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DebugLogger

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReleaseLogger