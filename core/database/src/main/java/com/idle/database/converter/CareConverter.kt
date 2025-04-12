package com.idle.database.converter

import androidx.room.TypeConverter
import com.idle.common.parseDateTime
import java.time.LocalDateTime

class CareConverter {
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateString: String?): LocalDateTime? {
        return dateString?.parseDateTime()
    }
}
