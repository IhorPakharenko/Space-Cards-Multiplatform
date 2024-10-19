package com.isao.yfoo3.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.scope.Scope

actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/" + AppDatabase.DB_FILE_NAME
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}

private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}