package com.isao.yfoo3.core.database

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.scope.Scope

actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = get<Application>()
    val dbFile = appContext.getDatabasePath(AppDatabase.DB_FILE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}