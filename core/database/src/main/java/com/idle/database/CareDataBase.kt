package com.idle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idle.database.converter.CareConverter
import com.idle.database.dao.MessagesDao
import com.idle.database.model.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1,
)
@TypeConverters(CareConverter::class)
internal abstract class CareDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao

    companion object {
        internal const val NAME = "care-database"
    }
}
