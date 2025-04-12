package com.idle.database.di

import android.content.Context
import androidx.room.Room
import com.idle.database.CareDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesPieceDatabase(
        @ApplicationContext context: Context,
    ): CareDatabase = Room.databaseBuilder(
        context,
        CareDatabase::class.java,
        CareDatabase.NAME,
    ).build()
}
