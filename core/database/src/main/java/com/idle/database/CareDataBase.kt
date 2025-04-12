package com.idle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idle.database.converter.CareConverter
import com.idle.database.dao.ChatRoomsDao
import com.idle.database.dao.MessagesDao
import com.idle.database.model.ChatRoomEntity
import com.idle.database.model.MessageEntity

@Database(
    entities = [
        MessageEntity::class,
        ChatRoomEntity::class,
    ],
    version = 1,
)
@TypeConverters(CareConverter::class)
internal abstract class CareDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
    abstract fun chatRoomsDao(): ChatRoomsDao

    companion object {
        internal const val NAME = "care-database"
    }
}
