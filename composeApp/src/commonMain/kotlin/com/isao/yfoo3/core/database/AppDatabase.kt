package com.isao.yfoo3.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.isao.yfoo3.data.local.dao.FeedImageDao
import com.isao.yfoo3.data.local.dao.LikedImageDao
import com.isao.yfoo3.data.local.model.FeedImageCached
import com.isao.yfoo3.data.local.model.LikedImageCached

@Database(entities = [FeedImageCached::class, LikedImageCached::class], version = 1)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedImageDao(): FeedImageDao

    abstract fun likedImageDao(): LikedImageDao

    companion object {
        internal const val DB_FILE_NAME = "app.db"
    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
