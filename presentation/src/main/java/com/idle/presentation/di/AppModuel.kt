package com.idle.presentation.di

import android.content.Context
import com.idle.presentation.network.NetworkObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkObserver(
        @ApplicationContext context: Context,
    ): NetworkObserver = NetworkObserver(context)
}