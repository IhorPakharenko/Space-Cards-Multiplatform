package com.isao.yfoo3.core.database

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class Converters {
    @TypeConverter
    fun instantToTimestamp(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }

    @TypeConverter
    fun fromTimestamp(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }
}