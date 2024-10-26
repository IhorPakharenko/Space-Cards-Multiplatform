package com.isao.yfoo3.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.isao.yfoo3.core.data.Database
import org.koin.core.scope.Scope

actual fun Scope.provideSqlDelightDriver(): SqlDriver {
    return AndroidSqliteDriver(Database.Schema, get(), "test.db")
}