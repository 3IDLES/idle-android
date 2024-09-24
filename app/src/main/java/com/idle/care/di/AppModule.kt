package com.idle.care.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideNotificationHandler(
        @ApplicationContext context: Context
    ): com.idle.care.notification.NotificationHandler =
        com.idle.care.notification.NotificationHandler(context)
}