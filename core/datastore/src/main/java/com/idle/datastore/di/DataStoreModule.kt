package com.idle.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val TOKEN_DATASTORE_NAME = "TOKENS_PREFERENCES"
    private val Context.tokenDataStore by preferencesDataStore(name = TOKEN_DATASTORE_NAME)

    private const val USER_INFO_DATASTORE_NAME = "USER_INFOS_PREFERENCES"
    private val Context.userInfoDataStore by preferencesDataStore(name = USER_INFO_DATASTORE_NAME)

    @Provides
    @Singleton
    @Named("token")
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.tokenDataStore

    @Provides
    @Singleton
    @Named("userInfo")
    fun provideUserInfoDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.userInfoDataStore
}