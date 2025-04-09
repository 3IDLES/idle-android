package com.idle.analytics.di

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.idle.analytics.BuildConfig
import com.idle.analytics.businessmetric.AmplitudeAnalyticsHelper
import com.idle.analytics.businessmetric.AnalyticsHelper
import com.idle.analytics.businessmetric.DebugAnalyticsHelper
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
    @Debug
    fun provideDebugAnalyticsHelper(): AnalyticsHelper = DebugAnalyticsHelper()

    @Provides
    @Singleton
    @Release
    fun provideReleaseAnalyticsHelper(amplitude: Amplitude): AnalyticsHelper =
        AmplitudeAnalyticsHelper(amplitude)

    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        @Debug debug: AnalyticsHelper,
        @Release release: AnalyticsHelper
    ): AnalyticsHelper {
        return if (BuildConfig.DEBUG) debug
        else release
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Debug

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Release
