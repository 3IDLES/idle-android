package com.idle.database.di

import com.idle.database.CareDatabase
import com.idle.database.dao.ChatRoomsDao
import com.idle.database.dao.MessagesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesMessagesDao(
        database: CareDatabase,
    ): MessagesDao = database.messagesDao()

    @Provides
    fun providesChatRoomsDao(
        database: CareDatabase,
    ): ChatRoomsDao = database.chatRoomsDao()
}
