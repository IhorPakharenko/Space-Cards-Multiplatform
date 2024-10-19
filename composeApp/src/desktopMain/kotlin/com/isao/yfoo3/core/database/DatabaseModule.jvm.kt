package com.isao.yfoo3.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.scope.Scope
import java.io.File

actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), AppDatabase.DB_FILE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}